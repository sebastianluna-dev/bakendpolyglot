using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using SeatLock.Domain;
using SeatLock.Http;
using SeatLock.Services;

namespace SeatLock.Controllers;

[ApiController]
[Authorize]
public sealed class HoldsController(HoldService holds, CheckoutService checkout, IdempotencyService idempotency, AppConfig config) : ControllerBase
{
    [HttpPost("holds")]
    [Authorize(Roles = "customer")]
    public async Task<IActionResult> Create(CancellationToken ct)
    {
        var body = await Body.Read(Request, "eventId", "seatIds");
        var eventId = body.Uuid("eventId");
        var seatIds = body.Uuids("seatIds", config.MaxSeatsPerHold);
        body.ThrowIfInvalid();

        var hold = await holds.Create(User.Sub(), eventId, seatIds, ct);
        return Created($"/holds/{hold.Id}", hold);
    }

    [HttpGet("holds/{id}")]
    public Task<HoldDto> Get(string id, CancellationToken ct) => holds.Get(User.Sub(), Ids.Parse(id), ct);

    [HttpDelete("holds/{id}")]
    public async Task<IActionResult> Release(string id, CancellationToken ct)
    {
        await holds.Release(User.Sub(), Ids.Parse(id), ct);
        return NoContent();
    }

    /// <summary>Checkout with an Idempotency-Key (Chapter 24); the stored outcome decides the status.</summary>
    [HttpPost("holds/{id}/checkout")]
    [Authorize(Roles = "customer")]
    public async Task<IActionResult> Checkout(string id, CancellationToken ct)
    {
        var holdId = Ids.Parse(id);
        var key = Request.Headers["Idempotency-Key"].ToString();
        if (key.Length is 0 or > 200) throw DomainException.IdempotencyKeyRequired();
        var user = User.Sub();

        var outcome = await idempotency.RunOnce(user, key, $"POST /holds/{holdId}/checkout", Request.Path.Value ?? "",
            () => checkout.Checkout(user, holdId, ct), StatusCodes.Status201Created, ct);
        if (outcome.Replayed) Response.Headers["Idempotent-Replayed"] = "true";
        if (outcome.Status == StatusCodes.Status201Created)
        {
            using var doc = System.Text.Json.JsonDocument.Parse(outcome.Body);
            Response.Headers.Location = $"/orders/{doc.RootElement.GetProperty("id").GetString()}";
        }
        return new ContentResult
        {
            StatusCode = outcome.Status,
            Content = outcome.Body,
            ContentType = outcome.Status >= 400 ? "application/problem+json" : "application/json",
        };
    }

    [HttpGet("orders/{id}")]
    public Task<OrderDto> Order(string id, CancellationToken ct) => checkout.Find(User.Sub(), Ids.Parse(id), ct);

    /// <summary>The reaper on demand. The hold-reaper client's service account has box-office.</summary>
    [HttpPost("admin/holds/release-expired")]
    [Authorize(Roles = "box-office")]
    public async Task<object> RunReaper(CancellationToken ct) => new { released = await holds.ReleaseExpired(ct) };
}
