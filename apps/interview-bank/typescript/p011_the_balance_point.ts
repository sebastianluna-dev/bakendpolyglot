// 011 · The Balance Point
import assert from "node:assert/strict";

export function balancePoint(nums: number[]): number {
  const total = nums.reduce((sum, x) => sum + x, 0);
  let left = 0;
  for (let i = 0; i < nums.length; i++) {
    if (left === total - left - nums[i]) return i;
    left += nums[i];
  }
  return -1;
}

assert.equal(balancePoint([3, 1, 5, 2, 2]), 2);
assert.equal(balancePoint([1, 2, 3]), -1);
assert.equal(balancePoint([]), -1);
assert.equal(balancePoint([7]), 0);
assert.equal(balancePoint([5, 2, -2]), 0);
assert.equal(balancePoint([0, 0, 0]), 0);
console.log("011 ok");
