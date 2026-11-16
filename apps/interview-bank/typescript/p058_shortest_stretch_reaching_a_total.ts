// 058 · Shortest Stretch Reaching a Total
import assert from "node:assert/strict";

export function shortestReaching(weights: number[], target: number): number {
  const n = weights.length;
  let best = n + 1; // longer than any real stretch: means "none found"
  let left = 0;
  let sum = 0;
  for (let right = 0; right < n; right++) {
    sum += weights[right];
    while (sum >= target) {
      best = Math.min(best, right - left + 1);
      sum -= weights[left];
      left++;
    }
  }
  return best === n + 1 ? 0 : best;
}

assert.equal(shortestReaching([3, 1, 4, 1, 5, 9, 2], 15), 3);
assert.equal(shortestReaching([2, 2, 2], 7), 0);
assert.equal(shortestReaching([], 5), 0);
assert.equal(shortestReaching([8], 8), 1);
assert.equal(shortestReaching([1, 1, 1, 1], 4), 4);
assert.equal(shortestReaching([1, 6, 1, 1, 7], 7), 1);
console.log("058 ok");
