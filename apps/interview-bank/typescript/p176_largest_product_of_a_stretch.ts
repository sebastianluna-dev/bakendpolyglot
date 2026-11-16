// 176 · Largest Product of a Stretch
import assert from "node:assert/strict";

export function largestProduct(nums: number[]): number {
  let best = nums[0];
  let hi = nums[0]; // largest product of a stretch that ends at the current element
  let lo = nums[0]; // smallest one: a negative number turns it into the largest
  for (let i = 1; i < nums.length; i++) {
    const x = nums[i];
    const a = hi * x;
    const b = lo * x;
    hi = Math.max(x, a, b);
    lo = Math.min(x, a, b);
    best = Math.max(best, hi);
  }
  return best;
}

assert.equal(largestProduct([2, -5, -2, 4, 0, 3]), 80);
assert.equal(largestProduct([-3, 0, -2]), 0);
assert.equal(largestProduct([-2]), -2);
assert.equal(largestProduct([-2, 3, -4]), 24);
assert.equal(largestProduct([0, 0]), 0);
assert.equal(largestProduct([-1, -1, -1]), 1);
assert.equal(largestProduct([3, -1, 4]), 4);
assert.equal(largestProduct([-4, 0, -5, 0, -6, 2]), 2);
assert.equal(largestProduct(new Array<number>(9).fill(10)), 1000000000);
console.log("176 ok");
