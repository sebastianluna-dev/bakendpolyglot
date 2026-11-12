// Build the PDF from the built site: serves dist/, lets Paged.js paginate /print/, prints to PDF.
// Usage: npm run build && npm run pdf
import { createServer } from 'node:http';
import { readFile, stat } from 'node:fs/promises';
import { extname, join } from 'node:path';
import puppeteer from 'puppeteer-core';

const DIST = new URL('../dist/', import.meta.url).pathname;
const OUT = join(DIST, 'backend-polyglot.pdf');
const TYPES = { '.html': 'text/html', '.js': 'text/javascript', '.css': 'text/css', '.woff2': 'font/woff2', '.woff': 'font/woff', '.svg': 'image/svg+xml', '.png': 'image/png', '.json': 'application/json' };

const server = createServer(async (req, res) => {
  let path = join(DIST, decodeURIComponent(new URL(req.url, 'http://x').pathname));
  try { if ((await stat(path)).isDirectory()) path = join(path, 'index.html'); } catch {}
  try {
    const body = await readFile(path);
    res.writeHead(200, { 'content-type': TYPES[extname(path)] ?? 'application/octet-stream' });
    res.end(body);
  } catch { res.writeHead(404); res.end(); }
}).listen(0);
const port = server.address().port;

const browser = await puppeteer.launch({ executablePath: process.env.CHROME || '/usr/bin/google-chrome', args: ['--no-sandbox'] });
const page = await browser.newPage();
page.setDefaultTimeout(600_000);
page.on('pageerror', (e) => console.error('[page error]', e.message));
page.on('console', (m) => { if (m.type() === 'error' || m.type() === 'warn') console.error('[console]', m.text()); });
await page.goto(`http://localhost:${port}/print/`, { waitUntil: 'load', timeout: 600_000 });
await page.waitForFunction('window.__pagedDone === true', { timeout: 600_000 });
await page.pdf({ path: OUT, preferCSSPageSize: true, printBackground: true });
const pages = await page.evaluate(() => document.querySelectorAll('.pagedjs_page').length);
await browser.close();
server.close();
console.log(`Wrote ${OUT} (${pages} pages)`);
