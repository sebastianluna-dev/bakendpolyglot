// The five stacks (plus framework variants) used across the book.
export const STACKS = {
  ts: { name: 'TypeScript', color: 'var(--bp-ts)' },
  express: { name: 'Express', color: 'var(--bp-ts)' },
  nest: { name: 'NestJS', color: 'var(--bp-ts)' },
  cs: { name: 'C#', color: 'var(--bp-cs)' },
  java: { name: 'Java', color: 'var(--bp-java)' },
  kotlin: { name: 'Kotlin', color: 'var(--bp-kotlin)' },
  go: { name: 'Go', color: 'var(--bp-go)' },
  py: { name: 'Python', color: 'var(--bp-py)' },
  django: { name: 'Django', color: 'var(--bp-py)' },
  fastapi: { name: 'FastAPI', color: 'var(--bp-py)' },
} as const;
export type StackId = keyof typeof STACKS;
