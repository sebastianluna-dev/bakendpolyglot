// 187 · Fewest Removals to Avoid Overlap
import assert from "node:assert/strict";

export function fewestRemovals(intervals: number[][]): number {
  const byEnd = intervals.toSorted((a, b) => a[1] - b[1]);
  let removals = 0;
  let lastEnd = -Infinity; // end of the last interval kept
  for (const [start, end] of byEnd) {
    if (start >= lastEnd) {
      lastEnd = end;
    } else {
      removals++;
    }
  }
  return removals;
}

assert.equal(fewestRemovals([[1, 10], [2, 3], [4, 5], [6, 7]]), 1);
assert.equal(fewestRemovals([[1, 3], [3, 5], [5, 8]]), 0);
assert.equal(fewestRemovals([]), 0);
assert.equal(fewestRemovals([[1, 2], [1, 2], [1, 2]]), 2);
assert.equal(fewestRemovals([[1, 4], [2, 3], [3, 6], [5, 7]]), 2);
console.log("187 ok");
