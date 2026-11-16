// 098 · Find a Peak
import assert from "node:assert/strict";

export function findPeak(altitudes: number[]): number {
  let lo = 0;
  let hi = altitudes.length - 1; // the last index can be the answer, but has no right neighbour to test
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    // Going down after mid: there is a peak at mid or to its left.
    if (altitudes[mid] > altitudes[mid + 1]) hi = mid;
    else lo = mid + 1;
  }
  return lo;
}

function isPeak(a: number[], i: number): boolean {
  return i >= 0 && i < a.length && (i === 0 || a[i - 1] < a[i]) && (i === a.length - 1 || a[i + 1] < a[i]);
}

const routes = [
  [120, 135, 150, 140, 160, 155],
  [10, 20, 30],
  [5],
  [3, 1],
  [1, 3],
  [5, 4, 3, 2, 1],
  [1, 3, 2, 4, 3, 5, 4],
  [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9],
];
for (const route of routes) assert.ok(isPeak(route, findPeak(route)), `no peak at ${findPeak(route)}`);
console.log("098 ok");
