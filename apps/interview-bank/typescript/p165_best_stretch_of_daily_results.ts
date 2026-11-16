// 165 · Best Stretch of Daily Results
import assert from "node:assert/strict";

export function bestStretch(results: number[]): number {
  let endingHere = 0; // best total of a stretch that ends on day r; 0 before day one
  let best = results[0];
  for (const r of results) {
    endingHere = Math.max(r, endingHere + r);
    best = Math.max(best, endingHere);
  }
  return best;
}

assert.equal(bestStretch([-2, 4, -1, 3, -7, 2]), 6);
assert.equal(bestStretch([-8, -3, -5]), -3);
assert.equal(bestStretch([5]), 5);
assert.equal(bestStretch([1, 2, 3]), 6);
assert.equal(bestStretch([-1, 0, -2]), 0);
assert.equal(bestStretch([2, -1, 2, -9, 4]), 4);
assert.equal(bestStretch([3, -2, 5, -1]), 6);
console.log("165 ok");
