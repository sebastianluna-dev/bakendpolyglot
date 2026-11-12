// POST /webhooks/payments (Chapter 24): verify the raw bytes, record the event id, apply idempotently.
// main.ts mounts express.raw() on this path, so req.body is the untouched Buffer.
import { createHmac, timingSafeEqual } from 'node:crypto';
import { Controller, HttpCode, Inject, Logger, Post, Req } from '@nestjs/common';
import type { Request } from 'express';
import { Public } from '../auth/auth.guard.js';
import { CONFIG, type Config } from '../config.js';
import { invalidBody, invalidSignature } from '../common/errors.js';
import { UUID } from '../common/validation.js';
import { PrismaService } from '../infra/prisma.service.js';

export function signatureMatches(raw: Buffer, given: string, secret: string): boolean {
  const expected = Buffer.from(createHmac('sha256', secret).update(raw).digest('hex'));
  const actual = Buffer.from(given);
  return actual.length === expected.length && timingSafeEqual(actual, expected);
}

interface PaymentEvent {
  id: string;
  type: string;
  data: { paymentIntentId: string; orderId?: unknown };
}

function parse(raw: Buffer): PaymentEvent {
  let e: any;
  try {
    e = JSON.parse(raw.toString('utf8'));
  } catch {
    throw invalidBody();
  }
  const ok = [e?.id, e?.type, e?.data?.paymentIntentId].every((v) => typeof v === 'string' && v.length > 0);
  if (!ok) throw invalidBody();
  return e as PaymentEvent;
}

interface LockedOrder {
  id: string;
  holdId: string;
  status: string;
}

@Controller('webhooks')
export class WebhooksController {
  private readonly log = new Logger('Webhooks');

  constructor(
    private readonly prisma: PrismaService,
    @Inject(CONFIG) private readonly config: Config,
  ) {}

  @Post('payments')
  @Public()
  @HttpCode(204)
  async payments(@Req() req: Request) {
    const raw = Buffer.isBuffer(req.body) ? req.body : Buffer.alloc(0);
    if (!signatureMatches(raw, req.get('x-signature') ?? '', this.config.PAYMENT_WEBHOOK_SECRET)) throw invalidSignature();
    const event = parse(raw);
    if (event.type !== 'payment_intent.succeeded' && event.type !== 'payment_intent.failed') return;

    const pi = event.data.paymentIntentId;
    const orderId = typeof event.data.orderId === 'string' && UUID.test(event.data.orderId) ? event.data.orderId : null;
    await this.prisma.$transaction(async (tx) => {
      const recorded = await tx.$executeRaw`INSERT INTO webhook_events (id, type) VALUES (${event.id}, ${event.type}) ON CONFLICT DO NOTHING`;
      if (recorded === 0) return; // duplicate delivery

      let [order] = await tx.$queryRaw<LockedOrder[]>`
        SELECT id, hold_id AS "holdId", status FROM orders WHERE payment_intent_id = ${pi} FOR UPDATE`;
      if (!order && orderId) {
        // The webhook can beat us to saving the intent id; the provider echoes our order id.
        [order] = await tx.$queryRaw<LockedOrder[]>`
          SELECT id, hold_id AS "holdId", status FROM orders WHERE id = ${orderId}::uuid AND payment_intent_id IS NULL FOR UPDATE`;
      }
      if (!order || order.status !== 'pending') {
        this.log.log(`payment event ${event.id} ignored (order ${order?.status ?? 'unknown'})`);
        return; // late or out-of-order events never undo a final state
      }
      if (event.type === 'payment_intent.succeeded') {
        await tx.$executeRaw`UPDATE orders SET status = 'paid', payment_intent_id = ${pi}, updated_at = now() WHERE id = ${order.id}::uuid`;
        await tx.$executeRaw`UPDATE seats SET status = 'sold' WHERE hold_id = ${order.holdId}::uuid`;
      } else {
        await tx.$executeRaw`UPDATE orders SET status = 'failed', payment_intent_id = ${pi}, updated_at = now() WHERE id = ${order.id}::uuid`;
        await tx.$executeRaw`UPDATE seats SET status = 'available', hold_id = NULL WHERE hold_id = ${order.holdId}::uuid`;
        await tx.$executeRaw`UPDATE holds SET status = 'released' WHERE id = ${order.holdId}::uuid`;
      }
    });
  }
}
