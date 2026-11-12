using SeatLock.Domain;

namespace SeatLock.Http;

public static class Ids
{
    /// <summary>Route ids bind as strings so a bad one is our invalid-id problem, not the framework's 400.</summary>
    public static Guid Parse(string raw) => Guid.TryParseExact(raw, "D", out var id) ? id : throw DomainException.InvalidId(raw);
}
