// 189 · Can You Reach the Last Square?
import assert from "node:assert/strict";

export function canReachLast(jumps: number[]): boolean {
  let furthest = 0;
  for (let i = 0; i < jumps.length; i++) {
    if (i > furthest) return false; // no reachable square jumps this far
    furthest = Math.max(furthest, i + jumps[i]);
  }
  return true;
}

assert.equal(canReachLast([2, 0, 3, 0, 0, 1]), true);
assert.equal(canReachLast([3, 1, 1, 0, 4]), false);
assert.equal(canReachLast([0]), true);
assert.equal(canReachLast([0, 1]), false);
assert.equal(canReachLast([4, 0, 0, 0, 0]), true);
assert.equal(canReachLast([1, 1, 0, 1]), false);
assert.equal(canReachLast([1, 2, 0, 0]), true);
console.log("189 ok");
