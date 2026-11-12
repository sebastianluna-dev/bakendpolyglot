import { Controller, Get, Module } from '@nestjs/common';
import { APP_GUARD } from '@nestjs/core';
import { AuthGuard, Public } from './auth/auth.guard.js';
import { CLOCK, CONFIG, loadConfig } from './config.js';
import { EventsController } from './events/events.controller.js';
import { EventsService } from './events/events.service.js';
import { HoldsController } from './holds/holds.controller.js';
import { HoldsService } from './holds/holds.service.js';
import { ReaperController, ReaperService } from './holds/reaper.service.js';
import { CacheService } from './infra/cache.service.js';
import { PrismaService } from './infra/prisma.service.js';
import { IdempotencyService } from './orders/idempotency.service.js';
import { OrdersController } from './orders/orders.controller.js';
import { OrdersService } from './orders/orders.service.js';
import { PaymentsClient } from './payments/payments.client.js';
import { WebhooksController } from './payments/webhooks.controller.js';

@Controller('health')
class HealthController {
  @Get()
  @Public()
  health() {
    return { status: 'ok' };
  }
}

// One module keeps the companion app easy to read; Chapter 28 covers splitting by feature.
@Module({
  controllers: [HealthController, EventsController, HoldsController, OrdersController, WebhooksController, ReaperController],
  providers: [
    { provide: CONFIG, useFactory: () => loadConfig() },
    { provide: CLOCK, useValue: () => new Date() },
    { provide: APP_GUARD, useClass: AuthGuard }, // deny by default (Chapter 21)
    PrismaService,
    CacheService,
    PaymentsClient,
    EventsService,
    HoldsService,
    OrdersService,
    IdempotencyService,
    ReaperService,
  ],
})
export class AppModule {}
