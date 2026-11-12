using Microsoft.EntityFrameworkCore;
using SeatLock.Data;
using SeatLock.Domain;

namespace SeatLock.Services;

public sealed record PaymentEvent(string Id, string Type, string PaymentIntentId, Guid? OrderId);

/// <summary>Applies verified payment events: dedupe by event id, then change only pending orders (Chapter 24).</summary>
public sealed class PaymentEvents(SeatLockDb db, ILogger<PaymentEvents> log)
{
    public async Task Apply(PaymentEvent e, CancellationToken ct)
    {
        await using var tx = await db.Database.BeginTransactionAsync(ct);
        var recorded = await db.Database.ExecuteSqlAsync($"INSERT INTO webhook_events (id, type) VALUES ({e.Id}, {e.Type}) ON CONFLICT DO NOTHING", ct);
        if (recorded == 0) return; // duplicate delivery

        var order = (await db.Orders.FromSql($"SELECT * FROM orders WHERE payment_intent_id = {e.PaymentIntentId} FOR UPDATE").ToListAsync(ct)).FirstOrDefault();
        if (order is null && e.OrderId is { } orderId)
        {
            // The webhook can beat us to saving the intent id; the provider echoes our order id.
            order = (await db.Orders.FromSql($"SELECT * FROM orders WHERE id = {orderId} AND payment_intent_id IS NULL FOR UPDATE").ToListAsync(ct)).FirstOrDefault();
        }
        if (order is null || order.Status != "pending")
        {
            log.LogInformation("payment event {Event} ignored (order {Status})", e.Id, order?.Status ?? "unknown");
            await tx.CommitAsync(ct);
            return; // late or out-of-order events never undo a final state
        }

        var holdId = order.HoldId;
        if (e.Type == "payment_intent.succeeded")
        {
            order.Status = "paid";
            await db.Seats.Where(s => s.HoldId == holdId).ExecuteUpdateAsync(u => u.SetProperty(s => s.Status, "sold"), ct);
        }
        else
        {
            order.Status = "failed";
            await db.Seats.Where(s => s.HoldId == holdId)
                .ExecuteUpdateAsync(u => u.SetProperty(s => s.Status, "available").SetProperty(s => s.HoldId, (Guid?)null), ct);
            await db.Holds.Where(h => h.Id == holdId).ExecuteUpdateAsync(u => u.SetProperty(h => h.Status, HoldStatus.Released), ct);
        }
        order.PaymentIntentId = e.PaymentIntentId;
        order.UpdatedAt = DateTimeOffset.UtcNow;
        await db.SaveChangesAsync(ct);
        await tx.CommitAsync(ct);
    }
}

/// <summary>Runs the reaper on a timer in-process (Chapter 24). Hangfire or a CronJob would also do.</summary>
public sealed class ReaperWorker(IServiceScopeFactory scopes, AppConfig config, ILogger<ReaperWorker> log) : BackgroundService
{
    protected override async Task ExecuteAsync(CancellationToken stoppingToken)
    {
        if (config.ReaperIntervalSeconds == 0) return;
        using var timer = new PeriodicTimer(TimeSpan.FromSeconds(config.ReaperIntervalSeconds));
        while (await timer.WaitForNextTickAsync(stoppingToken))
        {
            try
            {
                await using var scope = scopes.CreateAsyncScope();
                var released = await scope.ServiceProvider.GetRequiredService<HoldService>().ReleaseExpired(stoppingToken);
                if (released > 0) log.LogInformation("released {Count} expired holds", released);
            }
            catch (Exception e) when (e is not OperationCanceledException)
            {
                log.LogError(e, "reaper run failed");
            }
        }
    }
}
