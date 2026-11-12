import 'reflect-metadata';
import { NestFactory } from '@nestjs/core';
import type { NestExpressApplication } from '@nestjs/platform-express';
import express, { type NextFunction, type Request, type Response } from 'express';
import { AppModule } from './app.module.js';
import { ProblemFilter, writeProblem } from './common/problem.filter.js';
import { shelfwiseValidationPipe } from './common/validation.js';
import { CONFIG, type Config } from './config.js';
import { DomainError } from './domain/errors.js';

const app = await NestFactory.create<NestExpressApplication>(AppModule, { bodyParser: false });
app.set('etag', false); // ETags are version-based and set explicitly (Chapter 10)

// Our own JSON parser, so malformed bodies become a 400 invalid-body problem like every other stack.
app.use(express.json({ limit: '100kb' }));
app.use((err: { type?: string }, req: Request, res: Response, next: NextFunction) => {
  if (err.type !== 'entity.parse.failed') return next(err);
  writeProblem(res, new DomainError('invalid-body', 400, 'Malformed JSON', 'The request body is not valid JSON.'), req.originalUrl);
});

app.useGlobalPipes(shelfwiseValidationPipe());
app.useGlobalFilters(new ProblemFilter());
app.enableShutdownHooks(); // SIGTERM → onApplicationShutdown → pool.end() (Chapter 40)

const { PORT } = app.get<Config>(CONFIG);
await app.listen(PORT);
console.log(`shelfwise-nest listening on ${PORT}`);
