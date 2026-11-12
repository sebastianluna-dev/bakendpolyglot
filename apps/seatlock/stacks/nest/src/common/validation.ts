// A global ValidationPipe whose errors use the SeatLock 422 shape, and a pipe for UUID path ids.
import { Injectable, type PipeTransform, ValidationPipe, type ValidationError } from '@nestjs/common';
import { invalidId, validationFailed } from './errors.js';

function collect(errors: ValidationError[], prefix = '', out: Record<string, string[]> = {}) {
  for (const e of errors) {
    const field = prefix + e.property;
    for (const [rule, message] of Object.entries(e.constraints ?? {})) {
      (out[field] ??= []).push(rule === 'whitelistValidation' ? 'is not allowed' : message);
    }
    if (e.children?.length) collect(e.children, `${field}.`, out);
  }
  return out;
}

export const seatlockValidationPipe = () =>
  new ValidationPipe({
    whitelist: true,
    forbidNonWhitelisted: true, // unknown fields are an error, not silently dropped (Chapter 13)
    transform: true,
    exceptionFactory: (errors) => validationFailed(collect(errors)),
  });

export const UUID = /^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$/i;

@Injectable()
export class ParseUuidPipe implements PipeTransform<string, string> {
  transform(raw: string): string {
    if (!UUID.test(raw)) throw invalidId(raw);
    return raw.toLowerCase();
  }
}
