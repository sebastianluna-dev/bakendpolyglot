// 008 · Best Single Trade
import assert from "node:assert/strict";

export function bestTrade(prices: number[]): number {
  let lowest = Infinity;
  let best = 0;
  for (const price of prices) {
    lowest = Math.min(lowest, price);
    best = Math.max(best, price - lowest);
  }
  return best;
}

assert.equal(bestTrade([8, 3, 6, 1, 7, 4]), 6);
assert.equal(bestTrade([9, 7, 4, 2]), 0);
assert.equal(bestTrade([5, 9, 1]), 4);
assert.equal(bestTrade([3]), 0);
assert.equal(bestTrade([]), 0);
console.log("008 ok");
