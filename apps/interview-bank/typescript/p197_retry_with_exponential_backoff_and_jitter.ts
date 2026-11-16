// 197 · Retry With Exponential Backoff and Jitter
import assert from "node:assert/strict";

export function retry<T>(
  operation: () => T,
  maxAttempts: number,
  baseDelayMs: number,
  capMs: number,
  sleep: (ms: number) => void,
  random: () => number,
): T {
  let ceiling = baseDelayMs;
  for (let attempt = 1; attempt < maxAttempts; attempt++) {
    try {
      return operation();
    } catch {
      sleep(Math.floor(random() * ceiling)); // full jitter: anywhere in [0, ceiling)
      ceiling = Math.min(capMs, ceiling * 2);
    }
  }
  return operation(); // the last attempt: its error reaches the caller
}

function flaky(failures: number, result: string): () => string {
  let calls = 0;
  return () => {
    calls++;
    if (calls <= failures) throw new Error(`timeout ${calls}`);
    return result;
  };
}

function cycle(values: number[]): () => number {
  let calls = 0;
  return () => values[calls++ % values.length];
}

let delays: number[] = [];
const record = (ms: number) => {
  delays.push(ms);
};

assert.equal(retry(flaky(2, "receipt-42"), 5, 100, 1000, record, cycle([0.5])), "receipt-42");
assert.deepEqual(delays, [50, 100]);

delays = [];
const op = flaky(10, "never");
assert.throws(() => retry(op, 4, 100, 250, record, cycle([0.5, 0.25, 0.75])), { message: "timeout 4" });
assert.deepEqual(delays, [50, 50, 187]);

delays = [];
assert.equal(retry(flaky(0, "first try"), 3, 100, 1000, record, cycle([0.5])), "first try");
assert.deepEqual(delays, []);

delays = [];
assert.throws(() => retry(flaky(1, "late"), 1, 100, 1000, record, cycle([0.5])), { message: "timeout 1" });
assert.deepEqual(delays, []);

delays = [];
assert.equal(retry(flaky(6, "ok"), 7, 1000, 5000, record, cycle([0.999])), "ok");
assert.deepEqual(delays, [999, 1998, 3996, 4995, 4995, 4995]);
console.log("197 ok");
