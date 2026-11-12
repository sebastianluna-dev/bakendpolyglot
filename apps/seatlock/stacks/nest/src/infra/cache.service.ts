// Redis as a shared cache (Chapter 25). Optional, and never on the critical path: a Redis outage
// means a slower read, not a failed one.
import { Inject, Injectable, Logger, type OnModuleDestroy } from '@nestjs/common';
import { Redis } from 'ioredis';
import { CONFIG, type Config } from '../config.js';

@Injectable()
export class CacheService implements OnModuleDestroy {
  private readonly log = new Logger('Cache');
  private readonly redis?: Redis;

  constructor(@Inject(CONFIG) config: Config) {
    if (config.REDIS_URL) this.redis = new Redis(config.REDIS_URL, { maxRetriesPerRequest: 1 });
  }

  async get<T>(key: string): Promise<T | undefined> {
    try {
      const raw = await this.redis?.get(key);
      return raw ? (JSON.parse(raw) as T) : undefined;
    } catch (e) {
      this.log.warn(`cache read failed: ${String(e)}`);
      return undefined;
    }
  }

  async set(key: string, value: unknown, ttlSeconds: number): Promise<void> {
    try {
      await this.redis?.set(key, JSON.stringify(value), 'EX', ttlSeconds);
    } catch (e) {
      this.log.warn(`cache write failed: ${String(e)}`);
    }
  }

  onModuleDestroy() {
    this.redis?.disconnect();
  }
}
