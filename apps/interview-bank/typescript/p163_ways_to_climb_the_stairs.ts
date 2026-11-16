// 163 · Ways to Climb the Stairs
import assert from "node:assert/strict";

export function waysToClimb(n: number): number {
  let prev = 1; // ways to reach step i - 2
  let cur = 1; // ways to reach step i - 1
  for (let i = 2; i <= n; i++) {
    const next = prev + cur;
    prev = cur;
    cur = next;
  }
  return cur;
}

assert.equal(waysToClimb(4), 5);
assert.equal(waysToClimb(1), 1);
assert.equal(waysToClimb(0), 1);
assert.equal(waysToClimb(2), 2);
assert.equal(waysToClimb(10), 89);
assert.equal(waysToClimb(46), 2971215073);
assert.equal(waysToClimb(70), 308061521170129);
console.log("163 ok");
