// Typed configuration, validated at startup (Chapter 16). Invalid settings stop the process.
import { z } from 'zod';

const optional = z.string().optional().transform((v) => v || undefined);

const Env = z.object({
  PORT: z.coerce.number().int().min(1).max(65535).default(3000),
  DATABASE_URL: z.string().min(1),
  OIDC_ISSUER: z.url().transform((v) => v.replace(/\/$/, '')),
  OIDC_JWKS_URL: optional,
  OIDC_AUDIENCE: z.string().default('seatlock-api'),
  REDIS_URL: optional,
  PAYMENTS_URL: z.url().transform((v) => v.replace(/\/$/, '')),
  PAYMENT_WEBHOOK_SECRET: z.string().min(1),
  HOLD_TTL_SECONDS: z.coerce.number().int().min(1).max(3600).default(600),
  REAPER_INTERVAL_SECONDS: z.coerce.number().int().min(0).max(3600).default(60),
  MAX_SEATS_PER_HOLD: z.coerce.number().int().min(1).max(20).default(6),
});

export type Config = z.infer<typeof Env> & { OIDC_JWKS_URL: string };
export const CONFIG = Symbol('CONFIG');
export const CLOCK = Symbol('CLOCK');
export type Clock = () => Date;

export function loadConfig(env: NodeJS.ProcessEnv = process.env): Config {
  const parsed = Env.safeParse(env);
  if (!parsed.success) {
    const lines = parsed.error.issues.map((i) => `${i.path.join('.')}: ${i.message}`);
    throw new Error(`Invalid configuration:\n  ${lines.join('\n  ')}`);
  }
  const c = parsed.data;
  return { ...c, OIDC_JWKS_URL: c.OIDC_JWKS_URL ?? `${c.OIDC_ISSUER}/protocol/openid-connect/certs` };
}
