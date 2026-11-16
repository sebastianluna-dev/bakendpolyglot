// 156 · Combinations That Sum to a Target
import assert from "node:assert/strict";

export function combinationsToTarget(candidates: number[], target: number): number[][] {
  const sizes = candidates.toSorted((a, b) => a - b);
  const result: number[][] = [];
  const path: number[] = [];
  function walk(start: number, remaining: number): void {
    if (remaining === 0) {
      result.push([...path]);
      return;
    }
    for (let i = start; i < sizes.length; i++) {
      if (sizes[i] > remaining) break; // sorted: every later size is too big as well
      path.push(sizes[i]);
      walk(i, remaining - sizes[i]); // i, not i + 1: the same size may be used again
      path.pop();
    }
  }
  walk(0, target);
  return result;
}

assert.deepEqual(combinationsToTarget([2, 3, 5], 8), [[2, 2, 2, 2], [2, 3, 3], [3, 5]]);
assert.deepEqual(combinationsToTarget([7, 3, 2], 7), [[2, 2, 3], [7]]);
assert.deepEqual(combinationsToTarget([4, 6], 5), []);
assert.deepEqual(combinationsToTarget([1], 3), [[1, 1, 1]]);
assert.deepEqual(combinationsToTarget([9], 9), [[9]]);
assert.deepEqual(combinationsToTarget([5, 1], 6), [[1, 1, 1, 1, 1, 1], [1, 5]]);
console.log("156 ok");
