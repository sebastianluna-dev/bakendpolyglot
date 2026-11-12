// The hold reaper (Chapter 24): one statement, idempotent by construction. SKIP LOCKED lets several
// instances run it at once without waiting on each other. It also runs on a timer in-process.
import { Controller, HttpCode, Inject, Injectable, Logger, type OnApplicationBootstrap, type OnApplicationShutdown, Post } from '@nestjs/common';
import { Roles } from '../auth/auth.guard.js';
import { CLOCK, type Clock, CONFIG, type Config } from '../config.js';
import { PrismaService } from '../infra/prisma.service.js';

@Injectable()
export class ReaperService implements OnApplicationBootstrap, OnApplicationShutdown {
  private readonly log = new Logger('Reaper');
  private timer?: NodeJS.Timeout;

  constructor(
    private readonly prisma: PrismaService,
    @Inject(CONFIG) private readonly config: Config,
    @Inject(CLOCK) private readonly now: Clock,
  ) {}

  async releaseExpired(): Promise<number> {
    const [row] = await this.prisma.$queryRaw<{ released: number }[]>`
      WITH expired AS (
        SELECT id FROM holds WHERE status = 'active' AND expires_at <= ${this.now()}
        ORDER BY expires_at LIMIT 1000 FOR UPDATE SKIP LOCKED
      ), done AS (
        UPDATE holds h SET status = 'expired' FROM expired e WHERE h.id = e.id RETURNING h.id
      ), freed AS (
        UPDATE seats s SET status = 'available', hold_id = NULL FROM done WHERE s.hold_id = done.id AND s.status = 'held' RETURNING s.id
      )
      SELECT (SELECT count(*) FROM done)::int AS released, (SELECT count(*) FROM freed)::int AS seats`;
    return row?.released ?? 0;
  }

  onApplicationBootstrap() {
    const seconds = this.config.REAPER_INTERVAL_SECONDS;
    if (seconds === 0) return;
    this.timer = setInterval(() => {
      this.releaseExpired()
        .then((n) => n && this.log.log(`released ${n} expired holds`))
        .catch((e) => this.log.error(`reaper run failed: ${String(e)}`));
    }, seconds * 1000);
    this.timer.unref();
  }

  onApplicationShutdown() {
    clearInterval(this.timer);
  }
}

@Controller('admin/holds')
export class ReaperController {
  constructor(private readonly reaper: ReaperService) {}

  @Post('release-expired')
  @Roles('box-office') // the hold-reaper client's service account has it
  @HttpCode(200)
  async run() {
    return { released: await this.reaper.releaseExpired() };
  }
}
