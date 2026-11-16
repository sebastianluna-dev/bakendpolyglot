// 186 · Insert a New Interval
import assert from "node:assert/strict";

export function insertInterval(intervals: number[][], newInterval: number[]): number[][] {
  const result: number[][] = [];
  let [start, end] = newInterval;
  let i = 0;
  while (i < intervals.length && intervals[i][1] < start) result.push(intervals[i++]);
  while (i < intervals.length && intervals[i][0] <= end) {
    start = Math.min(start, intervals[i][0]);
    end = Math.max(end, intervals[i][1]);
    i++;
  }
  result.push([start, end]);
  while (i < intervals.length) result.push(intervals[i++]);
  return result;
}

assert.deepEqual(insertInterval([[1, 3], [6, 8], [10, 12], [15, 18]], [7, 10]), [[1, 3], [6, 12], [15, 18]]);
assert.deepEqual(insertInterval([[2, 4], [9, 11]], [5, 7]), [[2, 4], [5, 7], [9, 11]]);
assert.deepEqual(insertInterval([], [4, 6]), [[4, 6]]);
assert.deepEqual(insertInterval([[5, 6]], [1, 2]), [[1, 2], [5, 6]]);
assert.deepEqual(insertInterval([[1, 2]], [7, 9]), [[1, 2], [7, 9]]);
assert.deepEqual(insertInterval([[3, 4], [6, 7]], [1, 10]), [[1, 10]]);
assert.deepEqual(insertInterval([[1, 3]], [3, 5]), [[1, 5]]);
assert.deepEqual(insertInterval([[5, 8]], [2, 5]), [[2, 8]]);
console.log("186 ok");
