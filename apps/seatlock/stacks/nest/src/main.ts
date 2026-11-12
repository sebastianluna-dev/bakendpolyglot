import 'reflect-metadata';
import { NestFactory } from '@nestjs/core';
import type { NestExpressApplication } from '@nestjs/platform-express';
import express, { type NextFunction, type Request, type Response } from 'express';
import { randomUUID } from 'node:crypto';
import { AppModule } from './app.module.js';
import { DomainError } from './common/errors.js';
import { ProblemFilter, writeProblem } from './common/problem.filter.js';
import { seatlockValidationPipe } from './common/validation.js';
import { CONFIG, type Config } from './config.js';

const app = await NestFactory.create<NestExpressApplication>(AppModule, { bodyParser: false });

app.use((req: Request, res: Response, next: NextFunction) => {
  res.setHeader('x-request-id', req.get('x-request-id') || randomUUID());
  next();
});
// The webhook signature covers the raw bytes: keep them raw on that one route (Chapter 24).
app.use('/webhooks/payments', express.raw({ type: () => true, limit: '1mb' }));
app.use(express.json({ limit: '100kb' }));
app.use((err: { type?: string }, req: Request, res: Response, next: NextFunction) => {
  if (err.type !== 'entity.parse.failed') return next(err);
  writeProblem(res, new DomainError('invalid-body', 400, 'Malformed JSON', 'The request body is not valid JSON.'), req.originalUrl);
});

app.useGlobalPipes(seatlockValidationPipe());
app.useGlobalFilters(new ProblemFilter());
app.enableShutdownHooks();

const { PORT } = app.get<Config>(CONFIG);
await app.listen(PORT);
console.log(`seatlock-nest listening on ${PORT}`);
