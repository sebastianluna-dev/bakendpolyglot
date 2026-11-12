using Microsoft.EntityFrameworkCore;
using SeatLock.Data;
using SeatLock.Domain;

namespace SeatLock.Services;

public sealed class HoldService(SeatLockDb db, AppConfig config, TimeProvider clock)
{
    internal static HoldDto ToDto(Hold h, DateTimeOffset now) =>
        new(h.Id, h.EventId, h.SeatIds.Select(s => s.ToString()).Order(StringComparer.Ordinal).ToList(),
            HoldStatus.Visible(h.Status, h.ExpiresAt, now), h.ExpiresAt);

    /// <summary>All or nothing: one conditional UPDATE whose row count decides (Chapter 23).</summary>
    public async Task<HoldDto> Create(string userId, Guid eventId, List<Guid> seatIds, CancellationToken ct)
    {
        var now = clock.GetUtcNow();
        var ids = seatIds.ToArray();
        await using (var tx = await db.Database.BeginTransactionAsync(ct))
        {
            if (!await db.Events.AnyAsync(e => e.Id == eventId, ct)) throw DomainException.NotFound("Event", eventId);
            // Lock the requested rows in id order first, so overlapping holds cannot deadlock.
            await db.Database.ExecuteSqlAsync($"SELECT id FROM seats WHERE event_id = {eventId} AND id = ANY({ids}) ORDER BY id FOR UPDATE", ct);

            var hold = new Hold
            {
                Id = Guid.CreateVersion7(), EventId = eventId, UserId = userId, SeatIds = ids,
                CreatedAt = now, ExpiresAt = now.AddSeconds(config.HoldTtlSeconds),
            };
            db.Holds.Add(hold);
            await db.SaveChangesAsync(ct);

            var claimed = await db.Seats
                .Where(s => s.EventId == eventId && ids.Contains(s.Id) && s.Status == "available")
                .ExecuteUpdateAsync(u => u.SetProperty(s => s.Status, "held").SetProperty(s => s.HoldId, hold.Id), ct);
            if (claimed == ids.Length)
            {
                await tx.CommitAsync(ct);
                return ToDto(hold, now);
            }
        } // disposed without commit → rolled back

        db.ChangeTracker.Clear();
        var free = await db.Seats.Where(s => s.EventId == eventId && ids.Contains(s.Id) && s.Status == "available").Select(s => s.Id).ToListAsync(ct);
        throw DomainException.SeatsUnavailable(ids.Except(free));
    }

    /// <summary>Object-level authorization in the query: someone else's hold does not exist for you.</summary>
    public async Task<HoldDto> Get(string userId, Guid id, CancellationToken ct)
    {
        var hold = await db.Holds.AsNoTracking().FirstOrDefaultAsync(h => h.Id == id && h.UserId == userId, ct)
            ?? throw DomainException.NotFound("Hold", id);
        return ToDto(hold, clock.GetUtcNow());
    }

    public async Task Release(string userId, Guid id, CancellationToken ct)
    {
        var now = clock.GetUtcNow();
        await using var tx = await db.Database.BeginTransactionAsync(ct);
        var released = await db.Holds
            .Where(h => h.Id == id && h.UserId == userId && h.Status == HoldStatus.Active && h.ExpiresAt > now)
            .ExecuteUpdateAsync(u => u.SetProperty(h => h.Status, HoldStatus.Released), ct);
        if (released == 0)
            throw await db.Holds.AnyAsync(h => h.Id == id && h.UserId == userId, ct)
                ? DomainException.HoldNotActive(id) : DomainException.NotFound("Hold", id);
        await db.Seats.Where(s => s.HoldId == id && s.Status == "held")
            .ExecuteUpdateAsync(u => u.SetProperty(s => s.Status, "available").SetProperty(s => s.HoldId, (Guid?)null), ct);
        await tx.CommitAsync(ct);
    }

    /// <summary>The reaper (Chapter 24): idempotent; SKIP LOCKED lets several instances share the work.</summary>
    public async Task<int> ReleaseExpired(CancellationToken ct)
    {
        var now = clock.GetUtcNow();
        await using var tx = await db.Database.BeginTransactionAsync(ct);
        var expired = await db.Holds
            .FromSql($"SELECT * FROM holds WHERE status = 'active' AND expires_at <= {now} ORDER BY expires_at LIMIT 1000 FOR UPDATE SKIP LOCKED")
            .AsNoTracking().ToListAsync(ct);
        var ids = expired.Select(h => h.Id).ToArray();
        if (ids.Length > 0)
        {
            await db.Holds.Where(h => ids.Contains(h.Id)).ExecuteUpdateAsync(u => u.SetProperty(h => h.Status, HoldStatus.Expired), ct);
            await db.Seats.Where(s => s.HoldId != null && ids.Contains(s.HoldId.Value) && s.Status == "held")
                .ExecuteUpdateAsync(u => u.SetProperty(s => s.Status, "available").SetProperty(s => s.HoldId, (Guid?)null), ct);
        }
        await tx.CommitAsync(ct);
        return ids.Length;
    }
}
