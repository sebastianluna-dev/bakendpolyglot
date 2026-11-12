using System.Security.Cryptography;
using System.Text;
using System.Text.Json;
using Microsoft.EntityFrameworkCore;
using SeatLock.Data;
using SeatLock.Domain;
using SeatLock.Http;

namespace SeatLock.Services;

public sealed record Outcome(int Status, string Body, bool Replayed);

/// <summary>Idempotency keys (Chapter 24). The primary key on (user_id, key) makes two retries unable to both run.</summary>
public sealed class IdempotencyService(SeatLockDb db)
{
    static readonly JsonSerializerOptions Json = new(JsonSerializerDefaults.Web);

    /// <summary>4xx outcomes are stored like successes; 5xx are not, so a retry after an outage runs again.</summary>
    public async Task<Outcome> RunOnce<T>(string userId, string key, string fingerprint, string instance, Func<Task<T>> operation, int okStatus, CancellationToken ct)
    {
        var hash = Convert.ToHexStringLower(SHA256.HashData(Encoding.UTF8.GetBytes(fingerprint)));
        var claimed = await db.Database.ExecuteSqlAsync(
            $"INSERT INTO idempotency_keys (key, user_id, request_hash) VALUES ({key}, {userId}, {hash}) ON CONFLICT DO NOTHING", ct);
        if (claimed == 0)
        {
            var stored = await db.IdempotencyKeys.AsNoTracking().FirstAsync(k => k.UserId == userId && k.Key == key, ct);
            if (stored.RequestHash != hash) throw DomainException.IdempotencyKeyReuse();
            if (stored.Status is null) throw DomainException.IdempotencyInProgress();
            return new Outcome(stored.Status.Value, stored.Response ?? "null", true);
        }

        int status;
        string body;
        try
        {
            body = JsonSerializer.Serialize(await operation(), Json);
            status = okStatus;
        }
        catch (DomainException e) when (e.Status < 500)
        {
            status = e.Status;
            body = JsonSerializer.Serialize(Problems.Body(e, instance), Json);
        }
        catch
        {
            await db.IdempotencyKeys.Where(k => k.UserId == userId && k.Key == key).ExecuteDeleteAsync(CancellationToken.None);
            throw;
        }
        await db.IdempotencyKeys.Where(k => k.UserId == userId && k.Key == key)
            .ExecuteUpdateAsync(u => u.SetProperty(k => k.Status, status).SetProperty(k => k.Response, body), ct);
        return new Outcome(status, body, false);
    }
}
