import { randomUUID } from 'node:crypto';
import express from 'express';
import type { Logger } from 'pino';
import { problemHandler } from './http/problems.js';
import { routes, type Deps } from './http/routes.js';

declare global {
  namespace Express {
    interface Request {
      log: Logger;
    }
  }
}

export function createApp(deps: Deps, logger: Logger) {
  const app = express();
  app.set('etag', false); // ETags are ours: version-based, set explicitly (Chapter 10)
  app.set('query parser', 'simple');

  // Request id (Chapter 11): accept the caller's, or create one; echo it; log with it.
  app.use((req, res, next) => {
    const id = req.get('x-request-id') ?? randomUUID();
    res.setHeader('x-request-id', id);
    req.log = logger.child({ requestId: id });
    const started = performance.now();
    res.on('finish', () =>
      req.log.info({ method: req.method, path: req.originalUrl, status: res.statusCode, ms: Math.round(performance.now() - started) }, 'request'),
    );
    next();
  });

  app.use(express.json({ limit: '100kb' }));
  app.use(routes(deps));
  app.use((req, res) => {
    res.status(404).type('application/problem+json').json({
      type: 'https://shelfwise.dev/problems/not-found', title: 'Resource not found', status: 404, instance: req.originalUrl,
    });
  });
  app.use(problemHandler); // Express 5 forwards rejected promises from async handlers here
  return app;
}
