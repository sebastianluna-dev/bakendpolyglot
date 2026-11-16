// 017 · Longest Run of Consecutive IDs
import assert from "node:assert/strict";

export function longestRun(ids: number[]): number {
  const present = new Set(ids);
  let best = 0;
  for (const id of present) {
    if (present.has(id - 1)) continue; // not the start of a run
    let length = 1;
    while (present.has(id + length)) length++;
    best = Math.max(best, length);
  }
  return best;
}

assert.equal(longestRun([104, 7, 101, 5, 103, 6, 102]), 4);
assert.equal(longestRun([9, 1, 9, 2, 2]), 2);
assert.equal(longestRun([]), 0);
assert.equal(longestRun([5]), 1);
assert.equal(longestRun([-1, 0, 1, 3]), 3);
assert.equal(longestRun([10, 30, 20]), 1);
console.log("017 ok");
