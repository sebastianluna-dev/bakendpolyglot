// 096 · Minimum of a Rotated Sorted Array
import assert from "node:assert/strict";

export function rotatedMinimum(times: number[]): number {
  const last = times[times.length - 1];
  let lo = 0;
  let hi = times.length;
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    // Values <= last belong to the run after the wrap, which starts at the minimum.
    if (times[mid] <= last) hi = mid;
    else lo = mid + 1;
  }
  return times[lo];
}

assert.equal(rotatedMinimum([900, 1020, 1200, 60, 300, 480]), 60);
assert.equal(rotatedMinimum([60, 300, 480]), 60);
assert.equal(rotatedMinimum([7]), 7);
assert.equal(rotatedMinimum([2, 1]), 1);
assert.equal(rotatedMinimum([3, 4, 5, 1, 2]), 1);
assert.equal(rotatedMinimum([2, 3, 4, 5, 1]), 1);
assert.equal(rotatedMinimum([5, 1, 2, 3, 4]), 1);
console.log("096 ok");
