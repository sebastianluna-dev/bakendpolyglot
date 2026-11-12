import { Inject, Injectable } from '@nestjs/common';
import { CLOCK, type Clock, CONFIG, type Config } from '../config.js';
import { holdNotActive, notFound, seatsUnavailable, validationFailed } from '../common/errors.js';
import { PrismaService } from '../infra/prisma.service.js';

export type HoldStatus = 'active' | 'checked_out' | 'released' | 'expired';

/** An active hold past its expiry is expired, whether or not the reaper has run yet. */
export const visibleStatus = (status: string, expiresAt: Date, now: Date): HoldStatus =>
  status === 'active' && expiresAt <= now ? 'expired' : (status as HoldStatus);

class SeatsTaken extends Error {}

@Injectable()
export class HoldsService {
  constructor(
    private readonly prisma: PrismaService,
    @Inject(CONFIG) private readonly config: Config,
    @Inject(CLOCK) private readonly now: Clock,
  ) {}

  private json(h: { id: string; eventId: string; seatIds: string[]; status: string; expiresAt: Date }) {
    return { id: h.id, eventId: h.eventId, seatIds: [...h.seatIds].sort(), status: visibleStatus(h.status, h.expiresAt, this.now()), expiresAt: h.expiresAt.toISOString() };
  }

  /** All or nothing: one conditional UPDATE whose row count decides (Chapter 23). */
  async create(userId: string, eventId: string, rawSeatIds: string[]) {
    const seatIds = rawSeatIds.map((s) => s.toLowerCase());
    if (seatIds.length > this.config.MAX_SEATS_PER_HOLD) throw validationFailed({ seatIds: [`at most ${this.config.MAX_SEATS_PER_HOLD} seats per hold`] });
    const now = this.now();
    try {
      const hold = await this.prisma.$transaction(async (tx) => {
        if (!(await tx.event.findUnique({ where: { id: eventId }, select: { id: true } }))) throw notFound('Event', eventId);
        // Lock the requested rows in id order first, so overlapping holds cannot deadlock.
        await tx.$queryRaw`SELECT id FROM seats WHERE event_id = ${eventId}::uuid AND id = ANY(${seatIds}::uuid[]) ORDER BY id FOR UPDATE`;
        const hold = await tx.hold.create({
          data: { eventId, userId, seatIds, createdAt: now, expiresAt: new Date(now.getTime() + this.config.HOLD_TTL_SECONDS * 1000) },
        });
        const claimed = await tx.$executeRaw`
          UPDATE seats SET status = 'held', hold_id = ${hold.id}::uuid
          WHERE event_id = ${eventId}::uuid AND id = ANY(${seatIds}::uuid[]) AND status = 'available'`;
        if (claimed !== seatIds.length) throw new SeatsTaken(); // throwing rolls the transaction back
        return hold;
      });
      return this.json(hold);
    } catch (e) {
      if (!(e instanceof SeatsTaken)) throw e;
      const free = await this.prisma.seat.findMany({ where: { eventId, id: { in: seatIds }, status: 'available' }, select: { id: true } });
      const freeIds = new Set(free.map((s) => s.id));
      throw seatsUnavailable(seatIds.filter((id) => !freeIds.has(id)).sort());
    }
  }

  /** Object-level authorization in the query: someone else's hold does not exist for you (Chapter 21). */
  async get(userId: string, id: string) {
    const hold = await this.prisma.hold.findFirst({ where: { id, userId } });
    if (!hold) throw notFound('Hold', id);
    return this.json(hold);
  }

  async release(userId: string, id: string) {
    await this.prisma.$transaction(async (tx) => {
      const released = await tx.$executeRaw`
        UPDATE holds SET status = 'released'
        WHERE id = ${id}::uuid AND user_id = ${userId} AND status = 'active' AND expires_at > ${this.now()}`;
      if (released === 0) {
        const exists = await tx.hold.findFirst({ where: { id, userId }, select: { id: true } });
        throw exists ? holdNotActive(id) : notFound('Hold', id);
      }
      await tx.$executeRaw`UPDATE seats SET status = 'available', hold_id = NULL WHERE hold_id = ${id}::uuid AND status = 'held'`;
    });
  }
}
