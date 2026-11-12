import { Body, Controller, Delete, Get, HttpCode, Param, Post, Res } from '@nestjs/common';
import type { Response } from 'express';
import { type Caller, CurrentUser, Roles } from '../auth/auth.guard.js';
import { ParseUuidPipe } from '../common/validation.js';
import { CreateHoldDto } from './holds.dto.js';
import { HoldsService } from './holds.service.js';

@Controller('holds')
export class HoldsController {
  constructor(private readonly holds: HoldsService) {}

  @Post()
  @Roles('customer')
  async create(@Body() dto: CreateHoldDto, @CurrentUser() user: Caller, @Res({ passthrough: true }) res: Response) {
    const hold = await this.holds.create(user.sub, dto.eventId.toLowerCase(), dto.seatIds);
    res.location(`/holds/${hold.id}`);
    return hold;
  }

  @Get(':id')
  get(@Param('id', ParseUuidPipe) id: string, @CurrentUser() user: Caller) {
    return this.holds.get(user.sub, id);
  }

  @Delete(':id')
  @HttpCode(204)
  async release(@Param('id', ParseUuidPipe) id: string, @CurrentUser() user: Caller) {
    await this.holds.release(user.sub, id);
  }
}
