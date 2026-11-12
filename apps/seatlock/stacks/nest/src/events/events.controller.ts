import { Body, Controller, Get, Header, Param, Post, Res } from '@nestjs/common';
import type { Response } from 'express';
import { type Caller, CurrentUser, Public, Roles } from '../auth/auth.guard.js';
import { ParseUuidPipe } from '../common/validation.js';
import { CreateEventDto } from './events.dto.js';
import { EventsService } from './events.service.js';

@Controller('events')
export class EventsController {
  constructor(private readonly events: EventsService) {}

  @Post()
  @Roles('box-office')
  async create(@Body() dto: CreateEventDto, @CurrentUser() user: Caller, @Res({ passthrough: true }) res: Response) {
    const event = await this.events.create(dto, user.sub);
    res.location(`/events/${event.id}`);
    return event;
  }

  @Get(':id')
  @Public()
  @Header('Cache-Control', 'public, max-age=60')
  get(@Param('id', ParseUuidPipe) id: string) {
    return this.events.get(id);
  }

  @Get(':id/seats')
  @Public()
  @Header('Cache-Control', 'no-store')
  seats(@Param('id', ParseUuidPipe) id: string) {
    return this.events.seatMap(id);
  }
}
