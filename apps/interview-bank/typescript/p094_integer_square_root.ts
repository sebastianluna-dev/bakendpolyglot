// 094 · Integer Square Root
import assert from "node:assert/strict";

export function integerSqrt(x: number): number {
  let lo = 0;
  let hi = x + 1; // looking for the first k with k * k > x; k = x + 1 always is one
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    if (mid * mid > x) hi = mid;
    else lo = mid + 1;
  }
  return lo - 1;
}

assert.equal(integerSqrt(17), 4);
assert.equal(integerSqrt(36), 6);
assert.equal(integerSqrt(0), 0);
assert.equal(integerSqrt(1), 1);
assert.equal(integerSqrt(2), 1);
assert.equal(integerSqrt(4), 2);
assert.equal(integerSqrt(2_147_395_599), 46_339);
assert.equal(integerSqrt(2_147_395_600), 46_340);
assert.equal(integerSqrt(2_147_483_647), 46_340);
console.log("094 ok");
