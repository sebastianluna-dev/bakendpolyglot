// Copies Paged.js into public/ and patches one crash: its CSS parser returns no media name
// for range syntax such as `@media (width >= 50rem)`, which Starlight's CSS uses.
import { mkdirSync, readFileSync, writeFileSync } from 'node:fs';
const src = readFileSync('node_modules/pagedjs/dist/paged.polyfill.js', 'utf8');
const patched = src.replace('let media = this.getMediaName(node);', 'let media = this.getMediaName(node) || [];');
if (patched === src) throw new Error('Paged.js patch did not apply; check the pagedjs version.');
mkdirSync('public/vendor', { recursive: true });
writeFileSync('public/vendor/paged.polyfill.js', patched);
