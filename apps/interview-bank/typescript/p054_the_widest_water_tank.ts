// 054 · The Widest Water Tank
import assert from "node:assert/strict";

export function widestTank(heights: number[]): number {
  let left = 0;
  let right = heights.length - 1;
  let best = 0;
  while (left < right) {
    const area = (right - left) * Math.min(heights[left], heights[right]);
    best = Math.max(best, area);
    // The shorter wall limits every tank that keeps it, so move it inward.
    if (heights[left] <= heights[right]) left++;
    else right--;
  }
  return best;
}

assert.equal(widestTank([2, 6, 3, 1, 5, 2]), 15);
assert.equal(widestTank([4, 4]), 4);
assert.equal(widestTank([7]), 0);
assert.equal(widestTank([]), 0);
assert.equal(widestTank([1, 2, 3, 4, 5]), 6);
assert.equal(widestTank([0, 0, 0]), 0);
assert.equal(widestTank([5, 1, 1, 1, 5]), 20);
console.log("054 ok");
