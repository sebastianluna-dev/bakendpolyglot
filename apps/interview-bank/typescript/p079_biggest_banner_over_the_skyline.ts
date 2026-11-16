// 079 · Biggest Banner Over the Skyline
import assert from "node:assert/strict";

export function biggestBanner(heights: number[]): number {
  let best = 0;
  const stack: number[] = []; // indices of rising heights
  for (let i = 0; i <= heights.length; i++) {
    const h = i < heights.length ? heights[i] : 0; // a zero at the end empties the stack
    while (stack.length > 0 && heights[stack[stack.length - 1]] >= h) {
      const top = stack.pop()!;
      const left = stack.length > 0 ? stack[stack.length - 1] : -1;
      best = Math.max(best, heights[top] * (i - left - 1));
    }
    stack.push(i);
  }
  return best;
}

assert.equal(biggestBanner([3, 1, 4, 5, 4, 2]), 12);
assert.equal(biggestBanner([2, 4, 2]), 6);
assert.equal(biggestBanner([]), 0);
assert.equal(biggestBanner([7]), 7);
assert.equal(biggestBanner([1, 2, 3, 4, 5]), 9);
assert.equal(biggestBanner([5, 4, 3, 2, 1]), 9);
assert.equal(biggestBanner([2, 1, 2]), 3);
assert.equal(biggestBanner([0, 0]), 0);
assert.equal(biggestBanner([4, 0, 4]), 4);
console.log("079 ok");
