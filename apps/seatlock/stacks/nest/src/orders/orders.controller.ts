import { Controller, Get, Headers, Param, Post, Req, Res } from '@nestjs/common';
import type { Request, Response } from 'express';
import { type Caller, CurrentUser, Roles } from '../auth/auth.guard.js';
import { idempotencyKeyRequired } from '../common/errors.js';
import { ParseUuidPipe } from '../common/validation.js';
import { IdempotencyService } from './idempotency.service.js';
import { OrdersService } from './orders.service.js';

@Controller()
export class OrdersController {
  constructor(
    private readonly orders: OrdersService,
    private readonly idempotency: IdempotencyService,
  ) {}

  // @Res() without passthrough: the status comes from the stored outcome, not from Nest's default 201.
  @Post('holds/:id/checkout')
  @Roles('customer')
  async checkout(
    @Param('id', ParseUuidPipe) holdId: string,
    @Headers('idempotency-key') key: string | undefined,
    @CurrentUser() user: Caller,
    @Req() req: Request,
    @Res() res: Response,
  ) {
    if (!key || key.length > 200) throw idempotencyKeyRequired();
    const outcome = await this.idempotency.runOnce(user.sub, key, `POST /holds/${holdId}/checkout`, req.originalUrl, () => this.orders.checkout(user.sub, holdId), 201);
    if (outcome.replayed) res.setHeader('Idempotent-Replayed', 'true');
    if (outcome.status === 201) res.location(`/orders/${(outcome.body as { id: string }).id}`);
    res
      .status(outcome.status)
      .type(outcome.status >= 400 ? 'application/problem+json' : 'application/json')
      .json(outcome.body);
  }

  @Get('orders/:id')
  get(@Param('id', ParseUuidPipe) id: string, @CurrentUser() user: Caller) {
    return this.orders.find(user.sub, id);
  }
}
