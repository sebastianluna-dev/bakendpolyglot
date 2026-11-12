// Input DTOs (Chapter 13). Shape rules only; business rules live in services.
import { z } from 'zod';
import { DomainError } from '../domain/errors.js';

const title = z.string().min(1).max(200);
const author = z.string().min(1).max(200);
const year = z
  .number()
  .int()
  .min(1450)
  .nullable()
  .refine((y) => y === null || y <= new Date().getFullYear(), { message: 'cannot be in the future' });

export const CreateBookRequest = z.strictObject({
  title,
  author,
  isbn: z.string().regex(/^97[89]\d{10}$/, 'must be an ISBN-13 starting with 978 or 979'),
  year: year.optional().transform((y) => y ?? null),
});

export const UpdateBookRequest = z
  .strictObject({ title: title.optional(), author: author.optional(), year: year.optional() })
  .refine((b) => Object.keys(b).length > 0, { message: 'at least one field is required', path: ['body'] });

export const CreateCopyRequest = z.strictObject({ barcode: z.string().min(1).max(40) });

export const CreateMemberRequest = z.strictObject({
  email: z.email().max(254),
  name: z.string().min(1).max(200),
});

export const CreateLoanRequest = z.strictObject({
  copyId: z.number().int().positive(),
  memberId: z.number().int().positive(),
});

const limit = z.coerce.number().int().min(1).max(100).default(20);

export const BookListQuery = z.object({
  q: z.string().trim().min(1).optional(),
  after: z.string().optional(),
  limit,
});

export const MemberLoansQuery = z.object({
  status: z.enum(['open', 'all']).default('all'),
  after: z.string().optional(),
  limit,
});

/** Parse or throw a 422 validation problem with one entry per field. */
export function parse<S extends z.ZodType>(schema: S, input: unknown): z.output<S> {
  const result = schema.safeParse(input);
  if (result.success) return result.data;
  const errors: Record<string, string[]> = {};
  for (const issue of result.error.issues) {
    const fields = issue.code === 'unrecognized_keys' ? issue.keys.map((k) => [...issue.path, k].join('.')) : [issue.path.join('.') || 'body'];
    const message = issue.code === 'unrecognized_keys' ? 'is not allowed' : issue.message;
    for (const f of fields) (errors[f] ??= []).push(message);
  }
  throw new DomainError('validation', 422, 'Request is invalid', 'One or more fields are invalid.', { errors });
}

/** Path ids must be positive integers; anything else is a 400, not a 404. */
export function parseId(raw: string | undefined): number {
  const id = Number(raw);
  if (!raw || !/^\d+$/.test(raw) || !Number.isSafeInteger(id) || id < 1) {
    throw new DomainError('invalid-id', 400, 'Invalid id', `"${raw}" is not a valid id.`);
  }
  return id;
}
