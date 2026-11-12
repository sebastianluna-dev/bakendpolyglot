using System.Security.Cryptography;
using System.Text;
using Microsoft.AspNetCore.Http;
using SeatLock.Controllers;
using SeatLock.Domain;
using SeatLock.Http;

namespace SeatLock.Tests;

/// <summary>Fast tests: no database, no identity provider, no web host.</summary>
public class UnitTests
{
    static HttpRequest Request(string json)
    {
        var ctx = new DefaultHttpContext();
        ctx.Request.Body = new MemoryStream(Encoding.UTF8.GetBytes(json));
        return ctx.Request;
    }

    [Fact]
    public async Task Body_reports_every_invalid_field_with_nested_paths()
    {
        var body = await Body.Read(Request("""{"name":"","sections":[{"name":"A","rows":27,"x":1}]}"""), "name", "sections");
        body.Str("name", 1, 200);
        foreach (var s in body.Objects("sections", 1, 10, "name", "rows", "seatsPerRow", "priceCents")) s.Int("rows", 1, 26);
        var e = Assert.Throws<DomainException>(body.ThrowIfInvalid);
        var errors = (IReadOnlyDictionary<string, List<string>>)e.Extensions["errors"]!;
        Assert.Contains("name", errors.Keys);
        Assert.Contains("sections[0].rows", errors.Keys);
        Assert.Contains("sections[0].x", errors.Keys);
    }

    [Fact]
    public async Task Body_rejects_malformed_json_as_invalid_body()
    {
        var e = await Assert.ThrowsAsync<DomainException>(() => Body.Read(Request("""{"name": """)));
        Assert.Equal("invalid-body", e.Code);
    }

    [Fact]
    public void An_active_hold_past_expiry_reads_as_expired()
    {
        var now = DateTimeOffset.Parse("2026-03-01T10:00:00Z");
        Assert.Equal(HoldStatus.Expired, HoldStatus.Visible(HoldStatus.Active, now.AddSeconds(-1), now));
        Assert.Equal(HoldStatus.Active, HoldStatus.Visible(HoldStatus.Active, now.AddMinutes(5), now));
        Assert.Equal(HoldStatus.CheckedOut, HoldStatus.Visible(HoldStatus.CheckedOut, now.AddHours(-1), now));
    }

    [Fact]
    public void Webhook_signature_is_the_hmac_of_the_raw_bytes()
    {
        var raw = Encoding.UTF8.GetBytes("""{"id":"evt_1"}""");
        var good = Convert.ToHexStringLower(HMACSHA256.HashData(Encoding.UTF8.GetBytes("secret"), raw));
        Assert.True(WebhooksController.SignatureMatches(raw, good, "secret"));
        Assert.False(WebhooksController.SignatureMatches(raw, good, "other"));
        Assert.False(WebhooksController.SignatureMatches(Encoding.UTF8.GetBytes("""{"id": "evt_1"}"""), good, "secret"));
        Assert.False(WebhooksController.SignatureMatches(raw, "", "secret"));
    }

    [Fact]
    public void Config_reports_every_problem_at_once()
    {
        var e = Assert.Throws<InvalidOperationException>(() => AppConfig.Load(k => k == "HOLD_TTL_SECONDS" ? "0" : null));
        Assert.Contains("DATABASE_URL", e.Message);
        Assert.Contains("HOLD_TTL_SECONDS", e.Message);
    }
}
