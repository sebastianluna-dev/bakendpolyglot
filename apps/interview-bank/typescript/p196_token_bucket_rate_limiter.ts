// 196 · Token-Bucket Rate Limiter
import assert from "node:assert/strict";

export class TokenBucket {
  capacity: number;
  refillPerSecond: number;
  tokens: number;
  lastMs = 0;

  constructor(capacity: number, refillPerSecond: number) {
    this.capacity = capacity * 1000; // in thousandths of a token, so the arithmetic stays exact
    this.refillPerSecond = refillPerSecond;
    this.tokens = this.capacity;
  }

  allow(nowMs: number): boolean {
    // one token per second is one thousandth per millisecond
    this.tokens = Math.min(this.capacity, this.tokens + (nowMs - this.lastMs) * this.refillPerSecond);
    this.lastMs = nowMs;
    if (this.tokens < 1000) return false;
    this.tokens -= 1000;
    return true;
  }
}

const allowAll = (bucket: TokenBucket, times: number[]) => times.map((t) => bucket.allow(t));

assert.deepEqual(
  allowAll(new TokenBucket(3, 1), [0, 0, 0, 0, 999, 1000, 1000, 5000, 5000, 5000, 5000]),
  [true, true, true, false, false, true, false, true, true, true, false],
);
assert.deepEqual(allowAll(new TokenBucket(2, 4), [100, 100, 200, 350, 400, 600]), [true, true, false, true, false, true]);
assert.deepEqual(
  allowAll(new TokenBucket(1, 1), [1_000_000_000, 1_000_000_000, 1_000_000_999, 1_000_001_000]),
  [true, false, false, true],
);
assert.deepEqual(allowAll(new TokenBucket(5, 10_000), [7, 7, 7, 7, 7, 7, 7]), [true, true, true, true, true, false, false]);
console.log("196 ok");
