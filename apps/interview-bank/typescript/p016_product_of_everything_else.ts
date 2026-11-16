// 016 · Product of Everything Else
import assert from "node:assert/strict";

export function productOfOthers(nums: number[]): number[] {
  const out = new Array<number>(nums.length);
  let left = 1;
  for (let i = 0; i < nums.length; i++) {
    out[i] = left; // product of everything before i
    left *= nums[i];
  }
  let right = 1;
  for (let i = nums.length - 1; i >= 0; i--) {
    out[i] *= right;
    right *= nums[i];
  }
  return out;
}

assert.deepEqual(productOfOthers([2, 3, 4, 5]), [60, 40, 30, 24]);
assert.deepEqual(productOfOthers([4, 0, 5]), [0, 20, 0]);
assert.deepEqual(productOfOthers([3, 0, 2, 0]), [0, 0, 0, 0]);
assert.deepEqual(productOfOthers([-1, 2, -3]), [-6, 3, -2]);
assert.deepEqual(productOfOthers([7]), [1]);
assert.deepEqual(productOfOthers([]), []);
console.log("016 ok");
