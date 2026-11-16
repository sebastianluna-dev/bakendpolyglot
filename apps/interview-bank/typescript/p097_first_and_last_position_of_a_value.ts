// 097 · First and Last Position of a Value
import assert from "node:assert/strict";

export function firstAndLast(days: number[], day: number): number[] {
  const first = firstAtLeast(days, day);
  if (first === days.length || days[first] !== day) return [-1, -1];
  return [first, firstAtLeast(days, day + 1) - 1]; // one before the first value >= day + 1
}

function firstAtLeast(a: number[], target: number): number {
  let lo = 0;
  let hi = a.length;
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    if (a[mid] >= target) hi = mid;
    else lo = mid + 1;
  }
  return lo;
}

const due = [3, 5, 5, 5, 8, 9];
assert.deepEqual(firstAndLast(due, 5), [1, 3]);
assert.deepEqual(firstAndLast([3, 5, 8], 6), [-1, -1]);
assert.deepEqual(firstAndLast([], 1), [-1, -1]);
assert.deepEqual(firstAndLast([7, 7, 7, 7], 7), [0, 3]);
assert.deepEqual(firstAndLast([1], 1), [0, 0]);
assert.deepEqual(firstAndLast([1, 2, 2], 2), [1, 2]);
assert.deepEqual(firstAndLast(due, 1), [-1, -1]);
assert.deepEqual(firstAndLast(due, 10), [-1, -1]);
assert.deepEqual(firstAndLast(due, 3), [0, 0]);
assert.deepEqual(firstAndLast(due, 9), [5, 5]);
console.log("097 ok");
