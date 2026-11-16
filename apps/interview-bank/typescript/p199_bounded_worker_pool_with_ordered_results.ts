// 199 · Bounded Worker Pool With Ordered Results
import assert from "node:assert/strict";
import { setTimeout as sleep } from "node:timers/promises";

export async function mapLimit<T, R>(items: T[], limit: number, fn: (item: T) => Promise<R>): Promise<R[]> {
  const results = new Array<R>(items.length);
  let next = 0;
  async function worker(): Promise<void> {
    while (next < items.length) {
      const i = next++; // no lock needed: nothing else runs until the next await
      results[i] = await fn(items[i]);
    }
  }
  const workers = Array.from({ length: Math.min(limit, items.length) }, () => worker());
  await Promise.all(workers);
  return results;
}

async function measure(durations: number[], limit: number): Promise<[number[], number]> {
  let running = 0;
  let peak = 0;
  const results = await mapLimit(durations, limit, async (ms) => {
    running++;
    peak = Math.max(peak, running);
    await sleep(ms);
    running--;
    return ms * 10;
  });
  return [results, peak];
}

const durations = [40, 20, 30, 20, 25, 20, 30, 20]; // later tasks often finish before earlier ones
assert.deepEqual(await measure(durations, 3), [[400, 200, 300, 200, 250, 200, 300, 200], 3]);
assert.deepEqual(await measure([20, 20, 20], 1), [[200, 200, 200], 1]);
assert.deepEqual(await measure([30, 20], 10), [[300, 200], 2]);
assert.deepEqual(await measure([], 4), [[], 0]);
console.log("199 ok");
