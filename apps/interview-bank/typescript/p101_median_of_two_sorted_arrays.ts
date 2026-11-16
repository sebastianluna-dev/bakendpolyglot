// 101 · Median of Two Sorted Arrays
import assert from "node:assert/strict";

export function medianOfTwoSorted(a: number[], b: number[]): number {
  if (a.length > b.length) return medianOfTwoSorted(b, a);
  const m = a.length;
  const n = b.length;
  const half = Math.floor((m + n + 1) / 2);
  let lo = 0;
  let hi = m; // m stands for "take all of a"
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2); // values of a in the left half
    const rest = half - mid;
    if (rest === 0 || b[rest - 1] <= a[mid]) hi = mid;
    else lo = mid + 1;
  }
  const i = lo;
  const j = half - i;
  const leftMax = Math.max(i > 0 ? a[i - 1] : -Infinity, j > 0 ? b[j - 1] : -Infinity);
  if ((m + n) % 2 === 1) return leftMax;
  const rightMin = Math.min(i < m ? a[i] : Infinity, j < n ? b[j] : Infinity);
  return (leftMax + rightMin) / 2;
}

assert.equal(medianOfTwoSorted([12, 18, 31], [9, 20, 25, 40]), 20);
assert.equal(medianOfTwoSorted([5, 9], [6, 30]), 7.5);
assert.equal(medianOfTwoSorted([], [4]), 4);
assert.equal(medianOfTwoSorted([1, 2], []), 1.5);
assert.equal(medianOfTwoSorted([2, 2, 2], [2, 2]), 2);
assert.equal(medianOfTwoSorted([1, 2], [3, 4, 5, 6]), 3.5);
assert.equal(medianOfTwoSorted([1, 3, 5, 7, 9], [2]), 4);
assert.equal(medianOfTwoSorted([-5, -1], [-3]), -3);
assert.equal(medianOfTwoSorted([10], [1, 2, 3]), 2.5);
console.log("101 ok");
