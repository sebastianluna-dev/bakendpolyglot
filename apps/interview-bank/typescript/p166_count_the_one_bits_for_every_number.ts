// 166 · Count the One Bits for Every Number
import assert from "node:assert/strict";

export function oneBitCounts(n: number): number[] {
  const bits = new Array<number>(n + 1).fill(0);
  for (let i = 1; i <= n; i++) {
    bits[i] = bits[i >> 1] + (i & 1); // i without its last bit, plus that bit
  }
  return bits;
}

assert.deepEqual(oneBitCounts(5), [0, 1, 1, 2, 1, 2]);
assert.deepEqual(oneBitCounts(0), [0]);
assert.deepEqual(oneBitCounts(1), [0, 1]);
assert.deepEqual(oneBitCounts(8), [0, 1, 1, 2, 1, 2, 2, 3, 1]);
assert.equal(oneBitCounts(1023)[1023], 10);
assert.equal(oneBitCounts(100000)[65535], 16);
console.log("166 ok");
