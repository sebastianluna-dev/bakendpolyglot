using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Mvc;
using SeatLock.Domain;
using SeatLock.Http;
using SeatLock.Services;

namespace SeatLock.Controllers;

[ApiController]
[Route("events")]
public sealed class EventsController(EventService events) : ControllerBase
{
    const int MaxSeatsPerEvent = 5000;

    [HttpPost]
    [Authorize(Roles = "box-office")]
    public async Task<IActionResult> Create(CancellationToken ct)
    {
        var body = await Body.Read(Request, "name", "venue", "startsAt", "sections");
        var name = body.Str("name", 1, 200);
        var venue = body.Str("venue", 1, 200);
        var startsAt = body.DateTime("startsAt");
        var sections = body.Objects("sections", 1, 10, "name", "rows", "seatsPerRow", "priceCents")
            .Select(s => new SectionInput(s.Str("name", 1, 40), s.Int("rows", 1, 26), s.Int("seatsPerRow", 1, 50), s.Int("priceCents", 0, 1_000_000)))
            .ToList();
        if (sections.Select(s => s.Name).Distinct().Count() != sections.Count) body.Fail("sections", "section names must be unique");
        if (sections.Sum(s => s.Rows * s.SeatsPerRow) > MaxSeatsPerEvent) body.Fail("sections", $"at most {MaxSeatsPerEvent} seats per event");
        body.ThrowIfInvalid();

        var created = await events.Create(new NewEvent(name, venue, startsAt, sections), User.Sub(), ct);
        return Created($"/events/{created.Id}", created);
    }

    [HttpGet("{id}")]
    [AllowAnonymous]
    public async Task<EventDto> Get(string id, CancellationToken ct)
    {
        var e = await events.Get(Ids.Parse(id), ct);
        Response.Headers.CacheControl = "public, max-age=60";
        return e;
    }

    [HttpGet("{id}/seats")]
    [AllowAnonymous]
    public async Task<SeatMapDto> Seats(string id, CancellationToken ct)
    {
        var map = await events.SeatMap(Ids.Parse(id), ct);
        Response.Headers.CacheControl = "no-store";
        return map;
    }
}
