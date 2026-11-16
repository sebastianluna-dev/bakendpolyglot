// 155 · All Orderings
import assert from "node:assert/strict";

export function allOrderings(nums: number[]): number[][] {
  const result: number[][] = [];
  const path: number[] = [];
  const used = new Array<boolean>(nums.length).fill(false);
  function walk(): void {
    if (path.length === nums.length) {
      result.push([...path]);
      return;
    }
    for (let i = 0; i < nums.length; i++) {
      if (used[i]) continue;
      used[i] = true;
      path.push(nums[i]);
      walk();
      path.pop();
      used[i] = false;
    }
  }
  walk();
  return result;
}

assert.deepEqual(allOrderings([1, 2, 3]), [[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]);
assert.deepEqual(allOrderings([3, 1, 2]), [[3, 1, 2], [3, 2, 1], [1, 3, 2], [1, 2, 3], [2, 3, 1], [2, 1, 3]]);
assert.deepEqual(allOrderings([]), [[]]);
assert.deepEqual(allOrderings([9]), [[9]]);
assert.deepEqual(allOrderings([5, -5]), [[5, -5], [-5, 5]]);
assert.equal(allOrderings([1, 2, 3, 4, 5, 6]).length, 720);
console.log("155 ok");
