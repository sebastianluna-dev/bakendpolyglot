// Composition root: the only place that knows every piece (Chapter 28).
import { pino } from 'pino';
import { createApp } from './app.js';
import { loadConfig } from './config.js';
import { BookRepository } from './db/books.js';
import { CopyRepository, MemberRepository } from './db/copies-members.js';
import { LoanRepository } from './db/loans.js';
import { createPool } from './db/pool.js';
import { LoanService } from './domain/loans.js';

const config = loadConfig();
const logger = pino({ level: config.LOG_LEVEL });
const db = createPool(config.DATABASE_URL);
const loans = new LoanRepository(db);

const app = createApp(
  {
    books: new BookRepository(db),
    copies: new CopyRepository(db),
    members: new MemberRepository(db),
    loans,
    loanService: new LoanService(loans, { loanDays: config.LOAN_DAYS, maxLoansPerMember: config.MAX_LOANS_PER_MEMBER }),
  },
  logger,
);

const server = app.listen(config.PORT, () => logger.info({ port: config.PORT }, 'shelfwise-express listening'));

// Graceful shutdown (Chapter 40): stop accepting, drain, close the pool.
for (const signal of ['SIGTERM', 'SIGINT'] as const) {
  process.on(signal, () => {
    logger.info({ signal }, 'shutting down');
    server.close(() => db.end().then(() => process.exit(0)));
  });
}
