using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Caching.Hybrid;
using SeatLock.Data;
using SeatLock.Domain;

namespace SeatLock.Services;

public sealed class EventService(SeatLockDb db, HybridCache cache)
{
    const string Rows = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public async Task<EventDto> Create(NewEvent input, string createdBy, CancellationToken ct)
    {
        var e = new Event
        {
            Id = Guid.CreateVersion7(), Name = input.Name, Venue = input.Venue, StartsAt = input.StartsAt,
            CreatedBy = createdBy, CreatedAt = DateTimeOffset.UtcNow,
        };
        foreach (var s in input.Sections)
            for (var r = 0; r < s.Rows; r++)
                for (var n = 1; n <= s.SeatsPerRow; n++)
                    e.Seats.Add(new Seat { Id = Guid.CreateVersion7(), EventId = e.Id, Section = s.Name, RowLabel = Rows[r].ToString(), Number = n, PriceCents = s.PriceCents });
        db.Events.Add(e);
        await db.SaveChangesAsync(ct); // one transaction for the event and its seats
        return new EventDto(e.Id, e.Name, e.Venue, e.StartsAt, e.Seats.Count);
    }

    /// <summary>HybridCache: in-memory plus Redis, with one loader per key (Chapter 25).</summary>
    public async Task<EventDto> Get(Guid id, CancellationToken ct) =>
        await cache.GetOrCreateAsync($"event:{id}:v1", async token =>
            await db.Events.AsNoTracking().Where(e => e.Id == id)
                .Select(e => new EventDto(e.Id, e.Name, e.Venue, e.StartsAt, e.Seats.Count))
                .FirstOrDefaultAsync(token) ?? throw DomainException.NotFound("Event", id),
            new HybridCacheEntryOptions { Expiration = TimeSpan.FromSeconds(60) }, cancellationToken: ct);

    /// <summary>The layout may come from the cache; availability never does.</summary>
    public async Task<SeatMapDto> SeatMap(Guid eventId, CancellationToken ct)
    {
        var layout = await cache.GetOrCreateAsync($"event:{eventId}:seats:v1", async token =>
        {
            if (!await db.Events.AnyAsync(e => e.Id == eventId, token)) throw DomainException.NotFound("Event", eventId);
            return await db.Seats.AsNoTracking().Where(s => s.EventId == eventId)
                .OrderBy(s => s.Section).ThenBy(s => s.RowLabel).ThenBy(s => s.Number)
                .Select(s => new SeatDto(s.Id, s.Section, s.RowLabel, s.Number, s.PriceCents, null))
                .ToListAsync(token);
        }, new HybridCacheEntryOptions { Expiration = TimeSpan.FromMinutes(5) }, cancellationToken: ct);

        var status = await db.Seats.AsNoTracking().Where(s => s.EventId == eventId).ToDictionaryAsync(s => s.Id, s => s.Status, ct);
        return new SeatMapDto(eventId, layout.Select(s => s with { Status = status.GetValueOrDefault(s.Id) }).ToList());
    }
}
