// 024 · The Smallest Missing Positive
import assert from "node:assert/strict";

export function smallestMissingPositive(nums: number[]): number {
  const n = nums.length;
  for (let i = 0; i < n; i++) {
    // keep swapping until slot i holds a value that has no slot or is already in its slot
    while (nums[i] >= 1 && nums[i] <= n && nums[nums[i] - 1] !== nums[i]) {
      const j = nums[i] - 1;
      [nums[i], nums[j]] = [nums[j], nums[i]];
    }
  }
  for (let i = 0; i < n; i++) {
    if (nums[i] !== i + 1) return i + 1;
  }
  return n + 1;
}

assert.equal(smallestMissingPositive([3, 1, -2, 2, 7]), 4);
assert.equal(smallestMissingPositive([5, 2, 2, 9]), 1);
assert.equal(smallestMissingPositive([]), 1);
assert.equal(smallestMissingPositive([1, 2, 3]), 4);
assert.equal(smallestMissingPositive([0, -1, -5]), 1);
assert.equal(smallestMissingPositive([1, 1]), 2);
assert.equal(smallestMissingPositive([2, 3, 4, 1]), 5);
assert.equal(smallestMissingPositive([2147483647, 1]), 2);
console.log("024 ok");
