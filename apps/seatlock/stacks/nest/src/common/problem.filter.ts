// One place turns every error into application/problem+json (Chapters 3 and 12).
import { type ArgumentsHost, Catch, type ExceptionFilter, HttpException, Logger } from '@nestjs/common';
import type { Request, Response } from 'express';
import { DomainError } from './errors.js';

const TYPE_BASE = 'https://seatlock.dev/problems/';

export const problemBody = (e: DomainError, instance: string) => ({
  type: TYPE_BASE + e.code,
  title: e.title,
  status: e.status,
  detail: e.message,
  instance,
  ...e.extensions,
});

export function writeProblem(res: Response, e: DomainError, instance: string) {
  if (e.status === 401 && e.code === 'unauthorized') res.setHeader('WWW-Authenticate', 'Bearer realm="seatlock"');
  res.status(e.status).type('application/problem+json').json(problemBody(e, instance));
}

@Catch()
export class ProblemFilter implements ExceptionFilter {
  private readonly log = new Logger('ProblemFilter');

  catch(exception: unknown, host: ArgumentsHost) {
    const req = host.switchToHttp().getRequest<Request>();
    const res = host.switchToHttp().getResponse<Response>();

    if (exception instanceof DomainError) return writeProblem(res, exception, req.originalUrl);
    if (exception instanceof HttpException) {
      const status = exception.getStatus();
      const code = status === 404 ? 'not-found' : 'http-error';
      return writeProblem(res, new DomainError(code, status, exception.message, exception.message), req.originalUrl);
    }
    this.log.error(exception);
    return writeProblem(
      res,
      new DomainError('internal', 500, 'Internal error', `Something went wrong. Quote request id ${res.getHeader('x-request-id')}.`),
      req.originalUrl,
    );
  }
}
