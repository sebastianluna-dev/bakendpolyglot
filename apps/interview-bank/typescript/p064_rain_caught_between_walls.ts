// 064 · Rain Caught Between Walls
import assert from "node:assert/strict";

export function trappedRain(heights: number[]): number {
  let left = 0;
  let right = heights.length - 1;
  let leftMax = 0;
  let rightMax = 0;
  let water = 0;
  while (left < right) {
    // The lower end decides: the far side has a wall at least this high.
    if (heights[left] < heights[right]) {
      leftMax = Math.max(leftMax, heights[left]);
      water += leftMax - heights[left];
      left++;
    } else {
      rightMax = Math.max(rightMax, heights[right]);
      water += rightMax - heights[right];
      right--;
    }
  }
  return water;
}

assert.equal(trappedRain([3, 0, 2, 0, 4]), 7);
assert.equal(trappedRain([5, 4, 3]), 0);
assert.equal(trappedRain([]), 0);
assert.equal(trappedRain([7]), 0);
assert.equal(trappedRain([2, 0, 2]), 2);
assert.equal(trappedRain([0, 2, 0, 1, 0, 3, 1, 0, 2]), 8);
assert.equal(trappedRain([4, 1, 1, 4]), 6);
assert.equal(trappedRain([3, 0, 1]), 1);
console.log("064 ok");
