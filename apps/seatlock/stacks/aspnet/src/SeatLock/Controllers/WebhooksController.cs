using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using SeatLock.Domain;
using SeatLock.Services;

namespace SeatLock.Controllers;

/// <summary>POST /webhooks/payments: no [FromBody] binding, so the raw bytes reach the signature check (Chapter 24).</summary>
[ApiController]
[AllowAnonymous]
public sealed class WebhooksController(PaymentEvents events, AppConfig config) : ControllerBase
{
    public static bool SignatureMatches(byte[] raw, string given, string secret)
    {
        var expected = Convert.ToHexStringLower(HMACSHA256.HashData(Encoding.UTF8.GetBytes(secret), raw));
        return CryptographicOperations.FixedTimeEquals(Encoding.ASCII.GetBytes(given), Encoding.ASCII.GetBytes(expected));
    }

    [HttpPost("webhooks/payments")]
    public async Task<IActionResult> Payments(CancellationToken ct)
    {
        using var ms = new MemoryStream();
        await Request.Body.CopyToAsync(ms, ct);
        var raw = ms.ToArray();
        if (!SignatureMatches(raw, Request.Headers["X-Signature"].ToString(), config.WebhookSecret)) throw DomainException.InvalidSignature();

        PaymentEvent e;
        try
        {
            using var doc = JsonDocument.Parse(raw);
            var root = doc.RootElement;
            var data = root.GetProperty("data");
            Guid? orderId = data.TryGetProperty("orderId", out var o) && o.ValueKind == JsonValueKind.String && Guid.TryParse(o.GetString(), out var g) ? g : null;
            e = new PaymentEvent(root.GetProperty("id").GetString()!, root.GetProperty("type").GetString()!, data.GetProperty("paymentIntentId").GetString()!, orderId);
            if (e.Id.Length == 0 || e.Type.Length == 0 || e.PaymentIntentId.Length == 0) throw new FormatException();
        }
        catch (Exception ex) when (ex is JsonException or KeyNotFoundException or InvalidOperationException or FormatException or NullReferenceException)
        {
            throw DomainException.InvalidBody();
        }

        if (e.Type is "payment_intent.succeeded" or "payment_intent.failed") await events.Apply(e, ct);
        return NoContent();
    }
}
