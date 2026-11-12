namespace SeatLock.Domain;

// Response shapes. System.Text.Json writes them camelCase.
public sealed record EventDto(Guid Id, string Name, string Venue, DateTimeOffset StartsAt, int SeatCount);

public sealed record SeatDto(Guid Id, string Section, string Row, int Number, int PriceCents, string? Status = null);

public sealed record SeatMapDto(Guid EventId, IReadOnlyList<SeatDto> Items);

public sealed record HoldDto(Guid Id, Guid EventId, IReadOnlyList<string> SeatIds, string Status, DateTimeOffset ExpiresAt);

public sealed record OrderDto(
    Guid Id, Guid HoldId, Guid EventId, IReadOnlyList<string> SeatIds, string Status, int TotalCents, string Currency, string? PaymentIntentId);

public sealed record SectionInput(string Name, int Rows, int SeatsPerRow, int PriceCents);

public sealed record NewEvent(string Name, string Venue, DateTimeOffset StartsAt, IReadOnlyList<SectionInput> Sections);

public static class HoldStatus
{
    public const string Active = "active", CheckedOut = "checked_out", Released = "released", Expired = "expired";

    /// <summary>An active hold past its expiry is expired, whether or not the reaper has run yet.</summary>
    public static string Visible(string status, DateTimeOffset expiresAt, DateTimeOffset now) =>
        status == Active && expiresAt <= now ? Expired : status;
}
