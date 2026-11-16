// 092 · Where Would It Go?
import assert from "node:assert/strict";

export function insertPosition(deadlines: number[], deadline: number): number {
  let lo = 0;
  let hi = deadlines.length; // the answer may be deadlines.length itself
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    if (deadlines[mid] >= deadline) hi = mid;
    else lo = mid + 1;
  }
  return lo;
}

const stops = [480, 540, 600, 720];
assert.equal(insertPosition(stops, 610), 3);
assert.equal(insertPosition([480, 540, 540, 600], 540), 1);
assert.equal(insertPosition([], 300), 0);
assert.equal(insertPosition(stops, 100), 0);
assert.equal(insertPosition(stops, 800), 4);
assert.equal(insertPosition(stops, 480), 0);
assert.equal(insertPosition(stops, 720), 3);
assert.equal(insertPosition([5, 5, 5], 5), 0);
console.log("092 ok");
