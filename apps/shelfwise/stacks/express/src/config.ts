// Typed configuration, validated at startup (Chapter 16). Invalid settings stop the process.
import { z } from 'zod';

const Env = z.object({
  PORT: z.coerce.number().int().min(1).max(65535).default(3000),
  DATABASE_URL: z.string().min(1),
  LOAN_DAYS: z.coerce.number().int().min(1).max(60).default(21),
  MAX_LOANS_PER_MEMBER: z.coerce.number().int().min(1).max(50).default(5),
  LOG_LEVEL: z.enum(['debug', 'info', 'warn', 'error']).default('info'),
});

export type Config = z.infer<typeof Env>;

export function loadConfig(env: NodeJS.ProcessEnv = process.env): Config {
  const parsed = Env.safeParse(env);
  if (!parsed.success) {
    const lines = parsed.error.issues.map((i) => `${i.path.join('.')}: ${i.message}`);
    throw new Error(`Invalid configuration:\n  ${lines.join('\n  ')}`);
  }
  return parsed.data;
}
