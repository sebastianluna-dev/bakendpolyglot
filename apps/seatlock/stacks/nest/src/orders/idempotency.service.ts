// Idempotency keys (Chapter 24): the primary key on (user_id, key) is what makes two concurrent
// retries unable to both run.
import { createHash } from 'node:crypto';
import { Injectable } from '@nestjs/common';
import { DomainError, idempotencyInProgress, idempotencyKeyReuse } from '../common/errors.js';
import { problemBody } from '../common/problem.filter.js';
import { PrismaService } from '../infra/prisma.service.js';
import { Prisma } from '../generated/prisma/client.js';

export interface Outcome {
  status: number;
  body: unknown;
  replayed: boolean;
}

@Injectable()
export class IdempotencyService {
  constructor(private readonly prisma: PrismaService) {}

  /** 4xx outcomes are stored like successes; 5xx are not, so a retry after an outage runs again. */
  async runOnce(userId: string, key: string, fingerprint: string, instance: string, operation: () => Promise<unknown>, okStatus: number): Promise<Outcome> {
    const requestHash = createHash('sha256').update(fingerprint).digest('hex');
    try {
      await this.prisma.idempotencyKey.create({ data: { userId, key, requestHash } });
    } catch (e) {
      if (!(e instanceof Prisma.PrismaClientKnownRequestError && e.code === 'P2002')) throw e;
      const stored = await this.prisma.idempotencyKey.findUniqueOrThrow({ where: { userId_key: { userId, key } } });
      if (stored.requestHash !== requestHash) throw idempotencyKeyReuse();
      if (stored.status === null) throw idempotencyInProgress();
      return { status: stored.status, body: stored.response, replayed: true };
    }

    let status: number;
    let body: unknown;
    try {
      body = await operation();
      status = okStatus;
    } catch (e) {
      if (!(e instanceof DomainError) || e.status >= 500) {
        await this.prisma.idempotencyKey.delete({ where: { userId_key: { userId, key } } });
        throw e;
      }
      status = e.status;
      body = problemBody(e, instance);
    }
    await this.prisma.idempotencyKey.update({ where: { userId_key: { userId, key } }, data: { status, response: body as Prisma.InputJsonValue } });
    return { status, body, replayed: false };
  }
}
