// Dev helper: screenshot a URL. Usage: node scripts/shot.mjs <url> <out.png> [width] [dark]
import puppeteer from 'puppeteer-core';
const [url, out, width = '1280', dark] = process.argv.slice(2);
const browser = await puppeteer.launch({ executablePath: process.env.CHROME || '/usr/bin/google-chrome', args: ['--no-sandbox'] });
const page = await browser.newPage();
await page.setViewport({ width: Number(width), height: 900 });
if (dark) await page.emulateMediaFeatures([{ name: 'prefers-color-scheme', value: 'dark' }]);
await page.goto(url, { waitUntil: 'networkidle0' });
await page.screenshot({ path: out, fullPage: true });
await browser.close();
