// 018 · Count Stretches With a Target Sum
import assert from "node:assert/strict";

export function countStretches(changes: number[], target: number): number {
  const seen = new Map<number, number>([[0, 1]]); // prefix sum -> how many times; 0 is the empty prefix
  let sum = 0;
  let count = 0;
  for (const change of changes) {
    sum += change;
    count += seen.get(sum - target) ?? 0;
    seen.set(sum, (seen.get(sum) ?? 0) + 1);
  }
  return count;
}

assert.equal(countStretches([1, 2, 3, -2, 2], 3), 4);
assert.equal(countStretches([0, 0, 0], 0), 6);
assert.equal(countStretches([], 0), 0);
assert.equal(countStretches([5], 3), 0);
assert.equal(countStretches([3], 3), 1);
assert.equal(countStretches([1, -1, 1, -1], 0), 4);
console.log("018 ok");
