// 047 · Merge Two Sorted Arrays in Place
import assert from "node:assert/strict";

export function mergeInPlace(a: number[], m: number, b: number[]): void {
  let i = m - 1;
  let j = b.length - 1;
  let k = m + b.length - 1; // next slot to fill, from the back
  while (j >= 0) {
    if (i >= 0 && a[i] > b[j]) a[k--] = a[i--];
    else a[k--] = b[j--];
  }
}

function merged(a: number[], m: number, b: number[]): number[] {
  mergeInPlace(a, m, b);
  return a;
}

assert.deepEqual(merged([1, 4, 7, 0, 0], 3, [2, 8]), [1, 2, 4, 7, 8]);
assert.deepEqual(merged([0, 0], 0, [3, 5]), [3, 5]);
assert.deepEqual(merged([2, 3], 2, []), [2, 3]);
assert.deepEqual(merged([1, 2, 2, 0, 0, 0], 3, [2, 2, 3]), [1, 2, 2, 2, 2, 3]);
assert.deepEqual(merged([-3, 5, 0], 2, [-4]), [-4, -3, 5]);
assert.deepEqual(merged([4, 5, 0], 2, [1]), [1, 4, 5]);
console.log("047 ok");
