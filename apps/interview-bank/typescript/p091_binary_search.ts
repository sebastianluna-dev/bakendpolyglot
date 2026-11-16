// 091 · Binary Search
import assert from "node:assert/strict";

export function binarySearch(ids: number[], target: number): number {
  let lo = 0;
  let hi = ids.length; // the first ID >= target is somewhere in [lo, hi)
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    if (ids[mid] >= target) hi = mid;
    else lo = mid + 1;
  }
  return lo < ids.length && ids[lo] === target ? lo : -1;
}

const ids = [3, 8, 15, 21, 40, 57];
assert.equal(binarySearch(ids, 21), 3);
assert.equal(binarySearch(ids, 10), -1);
assert.equal(binarySearch([], 5), -1);
assert.equal(binarySearch([7], 7), 0);
assert.equal(binarySearch(ids, 3), 0);
assert.equal(binarySearch(ids, 57), 5);
assert.equal(binarySearch(ids, 1), -1);
assert.equal(binarySearch(ids, 99), -1);
console.log("091 ok");
