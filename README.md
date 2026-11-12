# Backend Polyglot

From language to system in five stacks: TypeScript (Express, NestJS), C# (ASP.NET Core),
Java & Kotlin (Spring Boot), Go and Python (Django, FastAPI).

One source, two editions:

- **Web documentation** — Astro Starlight, with synced "Five Stacks" tabs, interactive quizzes and flip-card flashcards.
- **Print book (PDF)** — the same MDX, paginated with Paged.js and printed by headless Chrome.

## Commands

| Command | What it does |
| --- | --- |
| `npm run dev` | Web edition at `localhost:4321`; the print preview is at `/print/` |
| `npm run build` | Builds the web edition into `dist/` |
| `npm run book` | Builds, then writes `dist/backend-polyglot.pdf` |

`npm run pdf` needs Chrome; set `CHROME=/path/to/chrome` if it is not at `/usr/bin/google-chrome`.

## Writing a chapter

Chapters live in `src/content/docs/part-N/NN-slug.mdx` with this frontmatter:

```yaml
title: Transactions and Concurrency
chapter: 23
part: Part III · Security and Data under Load
kicker: Two buyers, one seat, one database.
sidebar: { order: 23 }
```

Components (import from `src/components/book`): `FiveStacks` + `Stack`, `Recall`, `FromTS`,
`Pitfall`, `InTheWild`, `InterviewLens`, `Figure`, `Quiz`, `Flashcards`.
Export the quiz as `export const quiz = [...]` so the print edition can collect the answers
into Appendix A. See `part-0/01-how-to-read.mdx` for every component in use.

## Design

Direction B, "Classic Engineering": Fraunces (display), Source Serif 4 (body), IBM Plex Sans
(labels), IBM Plex Mono (code), navy accent on warm paper. Tokens: `src/styles/tokens.css`.
