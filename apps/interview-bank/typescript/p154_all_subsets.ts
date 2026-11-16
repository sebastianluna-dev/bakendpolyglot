// 154 · All Subsets
import assert from "node:assert/strict";

export function allSubsets(nums: number[]): number[][] {
  const result: number[][] = [];
  const path: number[] = [];
  function walk(start: number): void {
    result.push([...path]); // a copy: path keeps changing
    for (let i = start; i < nums.length; i++) {
      path.push(nums[i]);
      walk(i + 1);
      path.pop();
    }
  }
  walk(0);
  return result;
}

assert.deepEqual(allSubsets([1, 2, 3]), [[], [1], [1, 2], [1, 2, 3], [1, 3], [2], [2, 3], [3]]);
assert.deepEqual(allSubsets([4, 1]), [[], [4], [4, 1], [1]]);
assert.deepEqual(allSubsets([]), [[]]);
assert.deepEqual(allSubsets([7]), [[], [7]]);
assert.deepEqual(allSubsets([-1, 0, 2]), [[], [-1], [-1, 0], [-1, 0, 2], [-1, 2], [0], [0, 2], [2]]);
assert.equal(allSubsets([0, 1, 2, 3, 4, 5, 6, 7, 8, 9]).length, 1024);
console.log("154 ok");
