// Estimated reading time of a page, from its MDX source.
// Prose is read at about 230 words a minute; code at half that speed, since code is read slowly.
// Problem-bank pages also count the solution in one language (the one the print edition shows),
// because <Solutions> pulls the code in from apps/interview-bank at build time.
import { BANK_PRINT_STACK } from './stacks';

const PROSE_WPM = 230;
const CODE_WPM = 115;

const solutionFiles = import.meta.glob<string>('/apps/interview-bank/python/*.py', {
  query: '?raw',
  import: 'default',
  eager: true,
});

const words = (text: string) => (text.match(/[\p{L}\p{N}][\p{L}\p{N}'’_-]*/gu) ?? []).length;

function bankCode(n: string): string {
  if (BANK_PRINT_STACK !== 'py') return ''; // the estimate uses Python; close enough for the other languages
  const path = Object.keys(solutionFiles).find((p) => p.includes(`/python/p${n}_`));
  return path ? solutionFiles[path] : '';
}

export function readingMinutes(source: string): number {
  let body = source.replace(/^---\n[\s\S]*?\n---\n/, ''); // frontmatter
  let code = '';
  body = body.replace(/```[^\n]*\n([\s\S]*?)```/g, (_, block: string) => {
    code += block + '\n';
    return ' ';
  });
  body = body.replace(/<Solutions n="(\d{3})" \/>/g, (_, n: string) => {
    code += bankCode(n) + '\n';
    return ' ';
  });
  body = body
    .replace(/^import .*$/gm, ' ')
    .replace(/^export const \w+ = \[[\s\S]*?^\];$/gm, ' ') // quiz and flashcard data
    .replace(/<svg[\s\S]*?<\/svg>/g, ' ')
    .replace(/<\/?[A-Za-z][^>]*>/g, ' '); // component tags; the text inside them is kept
  const minutes = words(body) / PROSE_WPM + words(code) / CODE_WPM;
  return Math.max(1, Math.round(minutes));
}

export function formatMinutes(minutes: number): string {
  if (minutes < 60) return `${minutes} min read`;
  const h = Math.floor(minutes / 60);
  const m = minutes % 60;
  return m === 0 ? `${h} h read` : `${h} h ${m} min read`;
}

// The short form for the sidebar badge: "6 min", "1h", "3h 13m".
export function shortMinutes(minutes: number): string {
  if (minutes < 60) return `${minutes} min`;
  const h = Math.floor(minutes / 60);
  const m = minutes % 60;
  return m === 0 ? `${h}h` : `${h}h ${m}m`;
}
