// Package domain holds SeatLock's types and errors. It imports no HTTP, SQL or Redis packages.
package domain

import "fmt"

// Error is a domain error; the HTTP layer renders it as Problem Details (Chapter 12).
type Error struct {
	Code       string
	Status     int
	Title      string
	Detail     string
	Extensions map[string]any
}

func (e *Error) Error() string { return e.Code + ": " + e.Detail }

func newError(code string, status int, title, detail string, ext map[string]any) *Error {
	return &Error{Code: code, Status: status, Title: title, Detail: detail, Extensions: ext}
}

func NotFound(what, id string) *Error {
	return newError("not-found", 404, "Resource not found", fmt.Sprintf("%s %s does not exist.", what, id), nil)
}

func InvalidID(raw string) *Error {
	return newError("invalid-id", 400, "Invalid id", fmt.Sprintf("%q is not a valid id.", raw), nil)
}

func InvalidBody() *Error {
	return newError("invalid-body", 400, "Malformed body", "The request body is not a valid JSON document of the expected shape.", nil)
}

func Validation(errors map[string][]string) *Error {
	return newError("validation", 422, "Request is invalid", "One or more fields are invalid.", map[string]any{"errors": errors})
}

func Unauthorized() *Error {
	return newError("unauthorized", 401, "Authentication required", "Send a valid bearer token.", nil)
}

func Forbidden() *Error {
	return newError("forbidden", 403, "Forbidden", "Your role does not allow this action.", nil)
}

func SeatsUnavailable(seatIDs []string) *Error {
	return newError("seats-unavailable", 409, "Seats are not available",
		"One or more seats are held or sold. Nothing was held.", map[string]any{"seatIds": seatIDs})
}

func HoldNotActive(id string) *Error {
	return newError("hold-not-active", 409, "Hold is not active", fmt.Sprintf("Hold %s was released or checked out.", id), nil)
}

func HoldExpired(id string) *Error {
	return newError("hold-expired", 410, "Hold expired", fmt.Sprintf("Hold %s expired; its seats are back on sale.", id), nil)
}

func IdempotencyKeyRequired() *Error {
	return newError("idempotency-key-required", 400, "Idempotency-Key required",
		"Send an Idempotency-Key header so the request can be retried safely.", nil)
}

func IdempotencyKeyReuse() *Error {
	return newError("idempotency-key-reuse", 422, "Idempotency-Key reused", "This key was already used for a different request.", nil)
}

func IdempotencyInProgress() *Error {
	return newError("idempotency-in-progress", 409, "Request in progress",
		"A request with this Idempotency-Key is still running; retry shortly.", nil)
}

func PaymentProviderUnavailable() *Error {
	return newError("payment-provider-unavailable", 502, "Payment provider unavailable",
		"The payment provider did not answer. Retry with the same Idempotency-Key.", nil)
}

func InvalidSignature() *Error {
	return newError("invalid-signature", 401, "Invalid signature", "X-Signature does not match the body.", nil)
}
