package dev.seatlock.domain;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/** A domain error; the HTTP layer renders it as Problem Details (Chapter 12). */
public final class DomainException extends RuntimeException {
    private final String code;
    private final int status;
    private final String title;
    private final Map<String, Object> extensions;

    public DomainException(String code, int status, String title, String detail, Map<String, Object> extensions) {
        super(detail);
        this.code = code;
        this.status = status;
        this.title = title;
        this.extensions = extensions;
    }

    private DomainException(String code, int status, String title, String detail) {
        this(code, status, title, detail, Map.of());
    }

    public String code() { return code; }
    public int status() { return status; }
    public String title() { return title; }
    public Map<String, Object> extensions() { return extensions; }

    public static DomainException notFound(String what, Object id) {
        return new DomainException("not-found", 404, "Resource not found", what + " " + id + " does not exist.");
    }
    public static DomainException invalidId(String raw) {
        return new DomainException("invalid-id", 400, "Invalid id", "\"" + raw + "\" is not a valid id.");
    }
    public static DomainException invalidBody() {
        return new DomainException("invalid-body", 400, "Malformed body", "The request body is not a valid JSON document of the expected shape.");
    }
    public static DomainException validation(Map<String, List<String>> errors) {
        return new DomainException("validation", 422, "Request is invalid", "One or more fields are invalid.", Map.of("errors", errors));
    }
    public static DomainException unauthorized() {
        return new DomainException("unauthorized", 401, "Authentication required", "Send a valid bearer token.");
    }
    public static DomainException forbidden() {
        return new DomainException("forbidden", 403, "Forbidden", "Your role does not allow this action.");
    }
    public static DomainException seatsUnavailable(Collection<UUID> seatIds) {
        return new DomainException("seats-unavailable", 409, "Seats are not available",
                "One or more seats are held or sold. Nothing was held.",
                Map.of("seatIds", seatIds.stream().map(UUID::toString).sorted().toList()));
    }
    public static DomainException holdNotActive(UUID id) {
        return new DomainException("hold-not-active", 409, "Hold is not active", "Hold " + id + " was released or checked out.");
    }
    public static DomainException holdExpired(UUID id) {
        return new DomainException("hold-expired", 410, "Hold expired", "Hold " + id + " expired; its seats are back on sale.");
    }
    public static DomainException idempotencyKeyRequired() {
        return new DomainException("idempotency-key-required", 400, "Idempotency-Key required",
                "Send an Idempotency-Key header so the request can be retried safely.");
    }
    public static DomainException idempotencyKeyReuse() {
        return new DomainException("idempotency-key-reuse", 422, "Idempotency-Key reused", "This key was already used for a different request.");
    }
    public static DomainException idempotencyInProgress() {
        return new DomainException("idempotency-in-progress", 409, "Request in progress",
                "A request with this Idempotency-Key is still running; retry shortly.");
    }
    public static DomainException paymentProviderUnavailable() {
        return new DomainException("payment-provider-unavailable", 502, "Payment provider unavailable",
                "The payment provider did not answer. Retry with the same Idempotency-Key.");
    }
    public static DomainException invalidSignature() {
        return new DomainException("invalid-signature", 401, "Invalid signature", "X-Signature does not match the body.");
    }
    public static DomainException internal(String requestId) {
        return new DomainException("internal", 500, "Internal error", "Something went wrong. Quote request id " + requestId + ".");
    }
}
