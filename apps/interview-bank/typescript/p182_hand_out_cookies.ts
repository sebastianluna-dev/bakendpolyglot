// 182 · Hand Out Cookies
import assert from "node:assert/strict";

export function contentChildren(greed: number[], sizes: number[]): number {
  greed.sort((a, b) => a - b);
  sizes.sort((a, b) => a - b);
  let child = 0; // the least greedy child still without a cookie
  for (const size of sizes) {
    if (child < greed.length && size >= greed[child]) child++;
  }
  return child;
}

assert.equal(contentChildren([5, 2, 8, 2], [3, 1, 6, 9]), 3);
assert.equal(contentChildren([1, 3], [3, 1]), 2);
assert.equal(contentChildren([5, 2, 8, 2], [3, 1, 6]), 2);
assert.equal(contentChildren([4, 4], []), 0);
assert.equal(contentChildren([], [1, 2]), 0);
assert.equal(contentChildren([10, 20], [1, 2, 3]), 0);
assert.equal(contentChildren([2, 2, 2], [2, 2]), 2);
assert.equal(contentChildren([1, 2, 3], [3]), 1);
console.log("182 ok");
