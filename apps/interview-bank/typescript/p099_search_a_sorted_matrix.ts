// 099 · Search a Sorted Matrix
import assert from "node:assert/strict";

export function searchSortedMatrix(shelves: number[][], target: number): boolean {
  const rows = shelves.length;
  const cols = rows === 0 ? 0 : shelves[0].length;
  const total = rows * cols;
  let lo = 0;
  let hi = total; // positions 0 .. total - 1, read row by row as one sorted array
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    if (shelves[Math.floor(mid / cols)][mid % cols] >= target) hi = mid;
    else lo = mid + 1;
  }
  return lo < total && shelves[Math.floor(lo / cols)][lo % cols] === target;
}

const branch = [
  [1, 4, 7, 11],
  [15, 18, 21, 26],
  [30, 34, 40, 52],
];
assert.equal(searchSortedMatrix(branch, 21), true);
assert.equal(searchSortedMatrix(branch, 13), false);
assert.equal(searchSortedMatrix([], 1), false);
assert.equal(searchSortedMatrix([[]], 1), false);
assert.equal(searchSortedMatrix([[5]], 5), true);
assert.equal(searchSortedMatrix([[5]], 6), false);
assert.equal(searchSortedMatrix(branch, 60), false);
assert.equal(searchSortedMatrix(branch, 0), false);
assert.equal(searchSortedMatrix(branch, 1), true);
assert.equal(searchSortedMatrix(branch, 52), true);
assert.equal(searchSortedMatrix(branch, 26), true);
assert.equal(searchSortedMatrix(branch, 30), true);
assert.equal(searchSortedMatrix([[1], [3], [5]], 3), true);
assert.equal(searchSortedMatrix([[1], [3], [5]], 4), false);
console.log("099 ok");
