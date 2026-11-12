// Every error leaves as application/problem+json (RFC 9457, Chapter 12).
import type { ErrorRequestHandler, Response } from 'express';
import { DomainError } from '../domain/errors.js';

const TYPE_BASE = 'https://shelfwise.dev/problems/';

export function sendProblem(res: Response, e: DomainError, instance: string) {
  res
    .status(e.status)
    .type('application/problem+json')
    .json({ type: TYPE_BASE + e.code, title: e.title, status: e.status, detail: e.message, instance, ...e.extensions });
}

export const problemHandler: ErrorRequestHandler = (err, req, res, _next) => {
  if (err instanceof DomainError) return sendProblem(res, err, req.originalUrl);

  // express.json() raises this for malformed bodies
  if ((err as { type?: string }).type === 'entity.parse.failed') {
    return sendProblem(res, new DomainError('invalid-body', 400, 'Malformed JSON', 'The request body is not valid JSON.'), req.originalUrl);
  }

  req.log.error({ err }, 'unhandled error');
  return sendProblem(
    res,
    new DomainError('internal', 500, 'Internal error', `Something went wrong. Quote request id ${res.getHeader('x-request-id')}.`),
    req.originalUrl,
  );
};
