// 185 · Merge Overlapping Intervals
import assert from "node:assert/strict";

export function mergeIntervals(intervals: number[][]): number[][] {
  const sorted = intervals.toSorted((a, b) => a[0] - b[0]);
  const merged: number[][] = [];
  for (const [start, end] of sorted) {
    const last = merged.at(-1);
    if (last !== undefined && start <= last[1]) {
      last[1] = Math.max(last[1], end);
    } else {
      merged.push([start, end]); // a new pair, so extending it never changes the input
    }
  }
  return merged;
}

assert.deepEqual(mergeIntervals([[9, 12], [1, 3], [2, 6], [8, 9]]), [[1, 6], [8, 12]]);
assert.deepEqual(mergeIntervals([[1, 10], [2, 3], [4, 5]]), [[1, 10]]);
assert.deepEqual(mergeIntervals([]), []);
assert.deepEqual(mergeIntervals([[5, 5]]), [[5, 5]]);
assert.deepEqual(mergeIntervals([[3, 4], [1, 2]]), [[1, 2], [3, 4]]);
assert.deepEqual(mergeIntervals([[4, 6], [1, 4], [6, 6]]), [[1, 6]]);
console.log("185 ok");
