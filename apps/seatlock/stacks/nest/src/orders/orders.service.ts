import { Inject, Injectable } from '@nestjs/common';
import { CLOCK, type Clock } from '../config.js';
import { holdExpired, holdNotActive, notFound } from '../common/errors.js';
import { PrismaService } from '../infra/prisma.service.js';
import { PaymentsClient } from '../payments/payments.client.js';

interface LockedHold {
  id: string;
  status: string;
  expiresAt: Date;
}

@Injectable()
export class OrdersService {
  constructor(
    private readonly prisma: PrismaService,
    private readonly payments: PaymentsClient,
    @Inject(CLOCK) private readonly now: Clock,
  ) {}

  /**
   * Commit a pending order first, then call the provider: never hold locks across a network call.
   * A checkout interrupted after the commit resumes on retry with the same order.
   */
  async checkout(userId: string, holdId: string) {
    const now = this.now();
    const order = await this.prisma.$transaction(async (tx) => {
      const [hold] = await tx.$queryRaw<LockedHold[]>`
        SELECT id, status, expires_at AS "expiresAt" FROM holds WHERE id = ${holdId}::uuid AND user_id = ${userId} FOR UPDATE`;
      if (!hold) throw notFound('Hold', holdId);
      if (hold.status === 'checked_out') {
        const existing = await tx.order.findUniqueOrThrow({ where: { holdId } });
        if (existing.paymentIntentId) throw holdNotActive(holdId);
        return existing; // resume: the provider call failed last time
      }
      if (hold.status === 'expired' || (hold.status === 'active' && new Date(hold.expiresAt) <= now)) throw holdExpired(holdId);
      if (hold.status !== 'active') throw holdNotActive(holdId);

      const seats = await tx.seat.findMany({ where: { holdId }, select: { id: true, priceCents: true } });
      const created = await tx.order.create({
        data: {
          holdId,
          userId,
          totalCents: seats.reduce((n, s) => n + s.priceCents, 0),
          lines: { create: seats.map((s) => ({ seatId: s.id, priceCents: s.priceCents })) },
        },
      });
      await tx.hold.update({ where: { id: holdId }, data: { status: 'checked_out' } });
      return created;
    });

    const intentId = await this.payments.createIntent(order.id, order.totalCents, order.currency);
    await this.prisma.order.updateMany({ where: { id: order.id, paymentIntentId: null }, data: { paymentIntentId: intentId, updatedAt: new Date() } });
    return this.find(userId, order.id);
  }

  async find(userId: string, id: string) {
    const o = await this.prisma.order.findFirst({
      where: { id, userId },
      include: { hold: { select: { eventId: true } }, lines: { select: { seatId: true }, orderBy: { seatId: 'asc' } } },
    });
    if (!o) throw notFound('Order', id);
    return {
      id: o.id,
      holdId: o.holdId,
      eventId: o.hold.eventId,
      seatIds: o.lines.map((l) => l.seatId),
      status: o.status,
      totalCents: o.totalCents,
      currency: o.currency,
      paymentIntentId: o.paymentIntentId,
    };
  }
}
