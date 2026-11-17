// Starlight route middleware: adds each page's estimated reading time to its sidebar link.
import { defineRouteMiddleware } from '@astrojs/starlight/route-data';
import { getCollection } from 'astro:content';
import { readingMinutes, shortMinutes } from './components/book/readingTime';

type Entry = { type: string; href?: string; badge?: unknown; entries?: Entry[] };

let times: Promise<Map<string, string>> | undefined; // computed once per build
const readingTimes = () =>
  (times ??= getCollection('docs').then(
    (docs) => new Map(docs.filter((d) => d.id !== 'index' && d.body).map((d) => [`/${d.id}/`, shortMinutes(readingMinutes(d.body!))])),
  ));

export const onRequest = defineRouteMiddleware(async (context) => {
  const map = await readingTimes();
  const base = import.meta.env.BASE_URL.replace(/\/$/, '');
  const visit = (entries: Entry[]) => {
    for (const e of entries) {
      if (e.type === 'group' && e.entries) visit(e.entries);
      else if (e.type === 'link' && e.href && !e.badge) {
        const text = map.get(e.href.slice(base.length));
        if (text) e.badge = { text, variant: 'default', class: 'bp-read-time' };
      }
    }
  };
  visit(context.locals.starlightRoute.sidebar as Entry[]);
});
