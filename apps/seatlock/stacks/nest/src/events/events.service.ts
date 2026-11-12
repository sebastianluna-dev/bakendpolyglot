import { Injectable } from '@nestjs/common';
import { notFound, validationFailed } from '../common/errors.js';
import { CacheService } from '../infra/cache.service.js';
import { PrismaService } from '../infra/prisma.service.js';
import type { CreateEventDto } from './events.dto.js';

const MAX_SEATS_PER_EVENT = 5000;
const ROWS = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ';

export interface EventJson {
  id: string;
  name: string;
  venue: string;
  startsAt: string;
  seatCount: number;
}

interface LayoutSeat {
  id: string;
  section: string;
  row: string;
  number: number;
  priceCents: number;
}

@Injectable()
export class EventsService {
  constructor(
    private readonly prisma: PrismaService,
    private readonly cache: CacheService,
  ) {}

  async create(dto: CreateEventDto, createdBy: string): Promise<EventJson> {
    const names = dto.sections.map((s) => s.name);
    if (new Set(names).size !== names.length) throw validationFailed({ sections: ['section names must be unique'] });
    const seatCount = dto.sections.reduce((n, s) => n + s.rows * s.seatsPerRow, 0);
    if (seatCount > MAX_SEATS_PER_EVENT) throw validationFailed({ sections: [`at most ${MAX_SEATS_PER_EVENT} seats per event`] });

    const event = await this.prisma.$transaction(async (tx) => {
      const e = await tx.event.create({ data: { name: dto.name, venue: dto.venue, startsAt: new Date(dto.startsAt), createdBy } });
      await tx.seat.createMany({
        data: dto.sections.flatMap((s) =>
          Array.from({ length: s.rows * s.seatsPerRow }, (_, i) => ({
            eventId: e.id,
            section: s.name,
            rowLabel: ROWS[Math.floor(i / s.seatsPerRow)]!,
            number: (i % s.seatsPerRow) + 1,
            priceCents: s.priceCents,
          })),
        ),
      });
      return e;
    });
    return { id: event.id, name: event.name, venue: event.venue, startsAt: event.startsAt.toISOString(), seatCount };
  }

  /** Public and cached: events never change after creation (Chapter 25). */
  async get(id: string): Promise<EventJson> {
    const key = `event:${id}:v1`;
    const cached = await this.cache.get<EventJson>(key);
    if (cached) return cached;
    const e = await this.prisma.event.findUnique({ where: { id }, include: { _count: { select: { seats: true } } } });
    if (!e) throw notFound('Event', id);
    const json = { id: e.id, name: e.name, venue: e.venue, startsAt: e.startsAt.toISOString(), seatCount: e._count.seats };
    await this.cache.set(key, json, 60);
    return json;
  }

  /** The layout may come from the cache; availability never does. */
  async seatMap(eventId: string) {
    const key = `event:${eventId}:seats:v1`;
    let layout = await this.cache.get<LayoutSeat[]>(key);
    if (!layout) {
      if (!(await this.prisma.event.findUnique({ where: { id: eventId }, select: { id: true } }))) throw notFound('Event', eventId);
      const seats = await this.prisma.seat.findMany({
        where: { eventId },
        orderBy: [{ section: 'asc' }, { rowLabel: 'asc' }, { number: 'asc' }],
        select: { id: true, section: true, rowLabel: true, number: true, priceCents: true },
      });
      layout = seats.map((s) => ({ id: s.id, section: s.section, row: s.rowLabel, number: s.number, priceCents: s.priceCents }));
      await this.cache.set(key, layout, 300);
    }
    const fresh = await this.prisma.seat.findMany({ where: { eventId }, select: { id: true, status: true } });
    const status = new Map(fresh.map((s) => [s.id, s.status]));
    return { eventId, items: layout.map((s) => ({ ...s, status: status.get(s.id) })) };
  }
}
