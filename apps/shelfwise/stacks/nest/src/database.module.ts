import { Global, Inject, Module, type OnApplicationShutdown } from '@nestjs/common';
import pg from 'pg';
import { CONFIG, type Config } from './config.js';

pg.types.setTypeParser(20, (v) => Number(v)); // bigint ids fit in a JS number
export const POOL = Symbol('POOL');

export function isUniqueViolation(err: unknown, constraint: string): boolean {
  const e = err as { code?: string; constraint?: string };
  return e?.code === '23505' && e.constraint === constraint;
}

@Global()
@Module({
  providers: [{ provide: POOL, inject: [CONFIG], useFactory: (c: Config) => new pg.Pool({ connectionString: c.DATABASE_URL, max: 10 }) }],
  exports: [POOL],
})
export class DatabaseModule implements OnApplicationShutdown {
  constructor(@Inject(POOL) private readonly pool: pg.Pool) {}

  async onApplicationShutdown() {
    await this.pool.end(); // graceful shutdown (Chapter 40)
  }
}
