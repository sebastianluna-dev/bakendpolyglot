// 095 · Search a Rotated Sorted Array
import assert from "node:assert/strict";

export function searchRotated(times: number[], target: number): number {
  const n = times.length;
  if (n === 0) return -1;
  const last = times[n - 1];
  let lo = 0;
  let hi = n;
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    if (times[mid] <= last) hi = mid;
    else lo = mid + 1;
  }
  const start = lo; // index of the minimum: [start, n) and [0, start) are both sorted
  const [from, to] = target <= last ? [start, n] : [0, start];
  const i = firstAtLeast(times, from, to, target);
  return i < to && times[i] === target ? i : -1;
}

function firstAtLeast(a: number[], lo: number, hi: number, target: number): number {
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    if (a[mid] >= target) hi = mid;
    else lo = mid + 1;
  }
  return lo;
}

const board = [900, 1020, 1200, 60, 300, 480];
assert.equal(searchRotated(board, 300), 4);
assert.equal(searchRotated(board, 600), -1);
assert.equal(searchRotated(board, 1020), 1);
assert.equal(searchRotated(board, 900), 0);
assert.equal(searchRotated(board, 480), 5);
assert.equal(searchRotated(board, 60), 3);
assert.equal(searchRotated(board, 1300), -1);
assert.equal(searchRotated(board, 10), -1);
assert.equal(searchRotated([1, 2, 3], 3), 2);
assert.equal(searchRotated([5], 5), 0);
assert.equal(searchRotated([5], 1), -1);
assert.equal(searchRotated([], 1), -1);
assert.equal(searchRotated([2, 1], 1), 1);
assert.equal(searchRotated([2, 1], 2), 0);
console.log("095 ok");
