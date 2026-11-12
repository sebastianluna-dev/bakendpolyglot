// Domain errors. The ProblemFilter maps each one to a Problem Details response (Chapter 12).
export class DomainError extends Error {
  constructor(
    readonly code: string,
    readonly status: number,
    readonly title: string,
    detail: string,
    readonly extensions: Record<string, unknown> = {},
  ) {
    super(detail);
  }
}

export const notFound = (what: string, id: string) => new DomainError('not-found', 404, 'Resource not found', `${what} ${id} does not exist.`);
export const invalidId = (raw: string) => new DomainError('invalid-id', 400, 'Invalid id', `"${raw}" is not a valid id.`);
export const invalidBody = () => new DomainError('invalid-body', 400, 'Malformed body', 'The request body is not a valid JSON document of the expected shape.');
export const validationFailed = (errors: Record<string, string[]>) =>
  new DomainError('validation', 422, 'Request is invalid', 'One or more fields are invalid.', { errors });
export const unauthorized = () => new DomainError('unauthorized', 401, 'Authentication required', 'Send a valid bearer token.');
export const forbidden = () => new DomainError('forbidden', 403, 'Forbidden', 'Your role does not allow this action.');
export const seatsUnavailable = (seatIds: string[]) =>
  new DomainError('seats-unavailable', 409, 'Seats are not available', 'One or more seats are held or sold. Nothing was held.', { seatIds });
export const holdNotActive = (id: string) => new DomainError('hold-not-active', 409, 'Hold is not active', `Hold ${id} was released or checked out.`);
export const holdExpired = (id: string) => new DomainError('hold-expired', 410, 'Hold expired', `Hold ${id} expired; its seats are back on sale.`);
export const idempotencyKeyRequired = () =>
  new DomainError('idempotency-key-required', 400, 'Idempotency-Key required', 'Send an Idempotency-Key header so the request can be retried safely.');
export const idempotencyKeyReuse = () =>
  new DomainError('idempotency-key-reuse', 422, 'Idempotency-Key reused', 'This key was already used for a different request.');
export const idempotencyInProgress = () =>
  new DomainError('idempotency-in-progress', 409, 'Request in progress', 'A request with this Idempotency-Key is still running; retry shortly.');
export const paymentProviderUnavailable = () =>
  new DomainError('payment-provider-unavailable', 502, 'Payment provider unavailable', 'The payment provider did not answer. Retry with the same Idempotency-Key.');
export const invalidSignature = () => new DomainError('invalid-signature', 401, 'Invalid signature', 'X-Signature does not match the body.');
