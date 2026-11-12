// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';
import { existsSync } from 'node:fs';

// Book parts, in reading order. A part shows in the sidebar once its folder has content.
const PARTS = [
  ['part-0', 'Part 0 · Orientation'],
  ['part-1', 'Part I · Five Languages'],
  ['part-2', 'Part II · Backend Foundations'],
  ['part-3', 'Part III · Security and Data under Load'],
  ['part-4', 'Part IV · Architecture'],
  ['part-5', 'Part V · Distributed and Real-time'],
  ['part-6', 'Part VI · Operations'],
  ['part-7', 'Part VII · System Design'],
  ['part-8', 'Part VIII · Senior Craft'],
  ['appendix', 'Appendices'],
];

export default defineConfig({
  site: 'https://example.com', // TODO: replace with the deployed docs URL
  integrations: [
    starlight({
      title: 'Backend Polyglot',
      description: 'From language to system in five stacks: TypeScript, C#, Java & Kotlin, Go and Python.',
      customCss: ['./src/styles/fonts.css', './src/styles/theme-b.css'],
      components: { PageTitle: './src/components/overrides/PageTitle.astro' },
      expressiveCode: {
        themes: ['github-light', 'github-dark'],
        defaultProps: { wrap: true, preserveIndent: true },
        styleOverrides: {
          borderRadius: '2px',
          codeFontFamily: "'IBM Plex Mono', ui-monospace, monospace",
          uiFontFamily: "'IBM Plex Sans', system-ui, sans-serif",
        },
      },
      sidebar: PARTS.filter(([dir]) => existsSync(`./src/content/docs/${dir}`)).map(([dir, label]) => ({
        label,
        items: [{ autogenerate: { directory: dir } }],
      })),
    }),
  ],
});
