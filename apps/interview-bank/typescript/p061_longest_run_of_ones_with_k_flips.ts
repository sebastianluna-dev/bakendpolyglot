// 061 · Longest Run of Ones With k Flips
import assert from "node:assert/strict";

export function longestOnes(seats: number[], k: number): number {
  let left = 0;
  let zeros = 0; // zeros inside seats[left..right]
  let best = 0;
  for (let right = 0; right < seats.length; right++) {
    if (seats[right] === 0) zeros++;
    while (zeros > k) {
      if (seats[left] === 0) zeros--;
      left++;
    }
    best = Math.max(best, right - left + 1);
  }
  return best;
}

assert.equal(longestOnes([1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1], 2), 6);
assert.equal(longestOnes([1, 0, 1, 1, 1, 0], 0), 3);
assert.equal(longestOnes([], 3), 0);
assert.equal(longestOnes([0, 0, 0], 0), 0);
assert.equal(longestOnes([0, 0, 0], 5), 3);
assert.equal(longestOnes([1, 0, 0, 1], 1), 2);
assert.equal(longestOnes([1, 1, 1], 1), 3);
console.log("061 ok");
