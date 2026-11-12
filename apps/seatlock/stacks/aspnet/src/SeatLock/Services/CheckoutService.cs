using System.Net.Http.Json;
using Microsoft.EntityFrameworkCore;
using SeatLock.Data;
using SeatLock.Domain;

namespace SeatLock.Services;

/// <summary>The payment provider, called outside any transaction with the order id as its idempotency key.</summary>
public sealed class PaymentsClient(HttpClient http)
{
    sealed record Intent(string Id);

    public async Task<string> CreateIntent(Guid orderId, int amountCents, string currency, CancellationToken ct)
    {
        try
        {
            using var req = new HttpRequestMessage(HttpMethod.Post, "payment-intents")
            {
                Content = JsonContent.Create(new { orderId, amountCents, currency }),
            };
            req.Headers.Add("Idempotency-Key", orderId.ToString());
            using var res = await http.SendAsync(req, ct);
            res.EnsureSuccessStatusCode();
            var intent = await res.Content.ReadFromJsonAsync<Intent>(ct);
            return intent?.Id ?? throw DomainException.PaymentProviderUnavailable();
        }
        catch (Exception e) when (e is HttpRequestException or TaskCanceledException or System.Text.Json.JsonException)
        {
            throw DomainException.PaymentProviderUnavailable();
        }
    }
}

public sealed class CheckoutService(SeatLockDb db, PaymentsClient payments, TimeProvider clock)
{
    /// <summary>
    /// Commit a pending order first, then call the provider: never hold locks across a network call.
    /// A checkout interrupted after the commit resumes on retry with the same order.
    /// </summary>
    public async Task<OrderDto> Checkout(string userId, Guid holdId, CancellationToken ct)
    {
        var now = clock.GetUtcNow();
        Order order;
        await using (var tx = await db.Database.BeginTransactionAsync(ct))
        {
            var hold = (await db.Holds.FromSql($"SELECT * FROM holds WHERE id = {holdId} AND user_id = {userId} FOR UPDATE").ToListAsync(ct))
                .FirstOrDefault() ?? throw DomainException.NotFound("Hold", holdId);

            if (hold.Status == HoldStatus.CheckedOut)
            {
                order = await db.Orders.FirstAsync(o => o.HoldId == holdId, ct);
                if (order.PaymentIntentId is not null) throw DomainException.HoldNotActive(holdId);
            }
            else
            {
                if (HoldStatus.Visible(hold.Status, hold.ExpiresAt, now) == HoldStatus.Expired) throw DomainException.HoldExpired(holdId);
                if (hold.Status != HoldStatus.Active) throw DomainException.HoldNotActive(holdId);

                var seats = await db.Seats.Where(s => s.HoldId == holdId).Select(s => new { s.Id, s.PriceCents }).ToListAsync(ct);
                order = new Order
                {
                    Id = Guid.CreateVersion7(), HoldId = holdId, UserId = userId, TotalCents = seats.Sum(s => s.PriceCents),
                    CreatedAt = now, UpdatedAt = now,
                    Lines = seats.Select(s => new OrderLine { SeatId = s.Id, PriceCents = s.PriceCents }).ToList(),
                };
                db.Orders.Add(order);
                hold.Status = HoldStatus.CheckedOut;
                await db.SaveChangesAsync(ct);
            }
            await tx.CommitAsync(ct);
        }

        var intent = await payments.CreateIntent(order.Id, order.TotalCents, order.Currency, ct);
        var orderId = order.Id;
        await db.Orders.Where(o => o.Id == orderId && o.PaymentIntentId == null)
            .ExecuteUpdateAsync(u => u.SetProperty(o => o.PaymentIntentId, intent).SetProperty(o => o.UpdatedAt, DateTimeOffset.UtcNow), ct);
        return await Find(userId, orderId, ct);
    }

    public async Task<OrderDto> Find(string userId, Guid id, CancellationToken ct)
    {
        var o = await db.Orders.AsNoTracking().Include(x => x.Hold).Include(x => x.Lines)
            .FirstOrDefaultAsync(x => x.Id == id && x.UserId == userId, ct) ?? throw DomainException.NotFound("Order", id);
        return new OrderDto(o.Id, o.HoldId, o.Hold.EventId,
            o.Lines.Select(l => l.SeatId.ToString()).Order(StringComparer.Ordinal).ToList(),
            o.Status, o.TotalCents, o.Currency, o.PaymentIntentId);
    }
}
