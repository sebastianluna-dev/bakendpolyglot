import { Inject, Injectable, type OnModuleDestroy } from '@nestjs/common';
import { PrismaPg } from '@prisma/adapter-pg';
import { CONFIG, type Config } from '../config.js';
import { PrismaClient } from '../generated/prisma/client.js';

// Prisma 7 talks to Postgres through the node-postgres driver adapter: no query-engine binary.
@Injectable()
export class PrismaService extends PrismaClient implements OnModuleDestroy {
  constructor(@Inject(CONFIG) config: Config) {
    super({
      adapter: new PrismaPg({ connectionString: config.DATABASE_URL, max: 20 }),
      // Interactive transactions wait for a pooled connection; under a load test the default 2 s is too short.
      transactionOptions: { maxWait: 10_000, timeout: 15_000 },
    });
  }

  async onModuleDestroy() {
    await this.$disconnect();
  }
}
