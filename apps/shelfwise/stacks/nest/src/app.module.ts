import { Controller, Get, Global, type MiddlewareConsumer, Module, type NestModule } from '@nestjs/common';
import { randomUUID } from 'node:crypto';
import type { NextFunction, Request, Response } from 'express';
import { BooksModule } from './books/books.module.js';
import { CONFIG, loadConfig } from './config.js';
import { DatabaseModule } from './database.module.js';
import { LoansModule } from './loans/loans.module.js';
import { MembersModule } from './members/members.module.js';

@Controller('health')
class HealthController {
  @Get()
  health() {
    return { status: 'ok' };
  }
}

@Global()
@Module({ providers: [{ provide: CONFIG, useFactory: () => loadConfig() }], exports: [CONFIG] })
class ConfigModule {}

/** Request id (Chapter 11): accept the caller’s or create one, and echo it. */
function requestId(req: Request, res: Response, next: NextFunction) {
  res.setHeader('x-request-id', req.get('x-request-id') ?? randomUUID());
  next();
}

@Module({
  imports: [ConfigModule, DatabaseModule, BooksModule, MembersModule, LoansModule],
  controllers: [HealthController],
})
export class AppModule implements NestModule {
  configure(consumer: MiddlewareConsumer) {
    consumer.apply(requestId).forRoutes('*path');
  }
}
