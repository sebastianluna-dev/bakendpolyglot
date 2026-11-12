import { defineConfig } from 'prisma/config';

// `prisma generate` needs no database; the URL only matters for `prisma db pull`.
export default defineConfig({
  schema: 'prisma/schema.prisma',
  datasource: { url: process.env.DATABASE_URL ?? 'postgres://app:app@localhost:5432/seatlock' },
});
