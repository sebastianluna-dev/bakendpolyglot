// 171 · Longest Rising Sequence
import assert from "node:assert/strict";

export function longestRising(nums: number[]): number {
  const tails: number[] = []; // tails[k] = smallest last value of a rising sequence of length k + 1
  for (const x of nums) {
    let lo = 0;
    let hi = tails.length;
    while (lo < hi) {
      // first tail >= x, so an equal value replaces instead of extending
      const mid = (lo + hi) >> 1;
      if (tails[mid] < x) lo = mid + 1;
      else hi = mid;
    }
    if (lo === tails.length) tails.push(x);
    else tails[lo] = x;
  }
  return tails.length;
}

assert.equal(longestRising([3, 1, 8, 2, 5, 9, 4]), 4);
assert.equal(longestRising([7, 7, 7]), 1);
assert.equal(longestRising([]), 0);
assert.equal(longestRising([5]), 1);
assert.equal(longestRising([9, 8, 7]), 1);
assert.equal(longestRising([1, 2, 3, 4]), 4);
assert.equal(longestRising([2, 2, 3, 3, 4]), 3);
assert.equal(longestRising([6, 2, 9, 3, 7, 4, 10, 5, 8]), 5);
assert.equal(longestRising([10, -3, 4, -1, 6, 2, 8]), 4);
console.log("171 ok");
