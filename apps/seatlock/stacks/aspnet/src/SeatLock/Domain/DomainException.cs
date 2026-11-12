namespace SeatLock.Domain;

/// <summary>A domain error; the HTTP layer renders it as Problem Details (Chapter 12).</summary>
public sealed class DomainException(
    string code, int status, string title, string detail,
    IReadOnlyDictionary<string, object?>? extensions = null) : Exception(detail)
{
    public string Code { get; } = code;
    public int Status { get; } = status;
    public string Title { get; } = title;
    public IReadOnlyDictionary<string, object?> Extensions { get; } = extensions ?? new Dictionary<string, object?>();

    static Dictionary<string, object?> Ext(string key, object? value) => new() { [key] = value };

    public static DomainException NotFound(string what, object id) => new("not-found", 404, "Resource not found", $"{what} {id} does not exist.");
    public static DomainException InvalidId(string raw) => new("invalid-id", 400, "Invalid id", $"\"{raw}\" is not a valid id.");
    public static DomainException InvalidBody() => new("invalid-body", 400, "Malformed body", "The request body is not a valid JSON document of the expected shape.");
    public static DomainException Validation(IReadOnlyDictionary<string, List<string>> errors) =>
        new("validation", 422, "Request is invalid", "One or more fields are invalid.", Ext("errors", errors));
    public static DomainException Unauthorized() => new("unauthorized", 401, "Authentication required", "Send a valid bearer token.");
    public static DomainException Forbidden() => new("forbidden", 403, "Forbidden", "Your role does not allow this action.");
    public static DomainException SeatsUnavailable(IEnumerable<Guid> seatIds) =>
        new("seats-unavailable", 409, "Seats are not available", "One or more seats are held or sold. Nothing was held.",
            Ext("seatIds", seatIds.Select(s => s.ToString()).Order(StringComparer.Ordinal).ToList()));
    public static DomainException HoldNotActive(Guid id) => new("hold-not-active", 409, "Hold is not active", $"Hold {id} was released or checked out.");
    public static DomainException HoldExpired(Guid id) => new("hold-expired", 410, "Hold expired", $"Hold {id} expired; its seats are back on sale.");
    public static DomainException IdempotencyKeyRequired() =>
        new("idempotency-key-required", 400, "Idempotency-Key required", "Send an Idempotency-Key header so the request can be retried safely.");
    public static DomainException IdempotencyKeyReuse() =>
        new("idempotency-key-reuse", 422, "Idempotency-Key reused", "This key was already used for a different request.");
    public static DomainException IdempotencyInProgress() =>
        new("idempotency-in-progress", 409, "Request in progress", "A request with this Idempotency-Key is still running; retry shortly.");
    public static DomainException PaymentProviderUnavailable() =>
        new("payment-provider-unavailable", 502, "Payment provider unavailable", "The payment provider did not answer. Retry with the same Idempotency-Key.");
    public static DomainException InvalidSignature() => new("invalid-signature", 401, "Invalid signature", "X-Signature does not match the body.");
    public static DomainException Internal(string requestId) => new("internal", 500, "Internal error", $"Something went wrong. Quote request id {requestId}.");
}
