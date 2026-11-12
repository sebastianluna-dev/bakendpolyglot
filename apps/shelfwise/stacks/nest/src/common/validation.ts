// A global ValidationPipe whose errors use the Shelfwise 422 shape, and a pipe for path ids.
import { Injectable, type PipeTransform, ValidationPipe, type ValidationError } from '@nestjs/common';
import { DomainError } from '../domain/errors.js';

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

export const validationFailed = (errors: Record<string, string[]>) =>
  new DomainError('validation', 422, 'Request is invalid', 'One or more fields are invalid.', { errors });

export const shelfwiseValidationPipe = () =>
  new ValidationPipe({
    whitelist: true,
    forbidNonWhitelisted: true, // unknown fields are an error, not silently dropped (mass assignment, Chapter 13)
    transform: true,
    exceptionFactory: (errors) => validationFailed(collect(errors)),
  });

@Injectable()
export class ParseIdPipe implements PipeTransform<string, number> {
  transform(raw: string): number {
    const id = Number(raw);
    if (!/^\d+$/.test(raw) || !Number.isSafeInteger(id) || id < 1) {
      throw new DomainError('invalid-id', 400, 'Invalid id', `"${raw}" is not a valid id.`);
    }
    return id;
  }
}
