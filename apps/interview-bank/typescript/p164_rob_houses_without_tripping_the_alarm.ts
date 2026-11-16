// 164 · Rob Houses Without Tripping the Alarm
import assert from "node:assert/strict";

export function maxHaul(cash: number[]): number {
  let skipPrev = 0; // best total over the houses before the previous one
  let best = 0; // best total over the houses seen so far
  for (const c of cash) {
    const next = Math.max(best, skipPrev + c);
    skipPrev = best;
    best = next;
  }
  return best;
}

assert.equal(maxHaul([6, 1, 2, 9]), 15);
assert.equal(maxHaul([5, 10, 4, 3, 8]), 18);
assert.equal(maxHaul([]), 0);
assert.equal(maxHaul([7]), 7);
assert.equal(maxHaul([2, 1, 1, 2]), 4);
assert.equal(maxHaul([4, 1, 2, 7, 5, 3, 1]), 14);
assert.equal(maxHaul([0, 0, 0]), 0);
console.log("164 ok");
