// 002 · Two Prices for a Budget
import assert from "node:assert/strict";

export function twoPrices(prices: number[], budget: number): number[] {
  const firstIndex = new Map<number, number>(); // price -> earliest index
  for (let j = 0; j < prices.length; j++) {
    const i = firstIndex.get(budget - prices[j]);
    if (i !== undefined) return [i, j];
    if (!firstIndex.has(prices[j])) firstIndex.set(prices[j], j);
  }
  return [];
}

assert.deepEqual(twoPrices([12, 7, 3, 9, 5], 12), [2, 3]);
assert.deepEqual(twoPrices([4, 4, 1], 8), [0, 1]);
assert.deepEqual(twoPrices([1, 2], 10), []);
console.log("002 ok");
