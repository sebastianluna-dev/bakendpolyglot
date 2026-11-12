import { defineConfig } from 'vitest/config';

export default defineConfig({
  test: {
    include: ['tests/**/*.test.ts'],
    testTimeout: 15_000,
    fileParallelism: false, // the loan-limit test counts a member's open loans
  },
});
