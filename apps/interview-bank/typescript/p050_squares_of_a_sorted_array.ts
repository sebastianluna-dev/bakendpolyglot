// 050 · Squares of a Sorted Array
import assert from "node:assert/strict";

export function sortedSquares(nums: number[]): number[] {
  const out = new Array<number>(nums.length);
  let lo = 0;
  let hi = nums.length - 1;
  for (let k = nums.length - 1; k >= 0; k--) {
    // the largest square left is at one of the two ends
    if (Math.abs(nums[lo]) > Math.abs(nums[hi])) {
      out[k] = nums[lo] * nums[lo];
      lo++;
    } else {
      out[k] = nums[hi] * nums[hi];
      hi--;
    }
  }
  return out;
}

assert.deepEqual(sortedSquares([-5, -2, 0, 3, 4]), [0, 4, 9, 16, 25]);
assert.deepEqual(sortedSquares([-3, -3, -1]), [1, 9, 9]);
assert.deepEqual(sortedSquares([]), []);
assert.deepEqual(sortedSquares([2, 5]), [4, 25]);
assert.deepEqual(sortedSquares([-7]), [49]);
assert.deepEqual(sortedSquares([-4, -1, 1, 4]), [1, 1, 16, 16]);
console.log("050 ok");
