// 179 · Best Profit With at Most k Trades
import assert from "node:assert/strict";

export function bestProfit(prices: number[], k: number): number {
  if (prices.length === 0 || k === 0) return 0;
  const bought = new Array<number>(k + 1).fill(-prices[0]); // bought[t]: most cash holding the t-th share
  const sold = new Array<number>(k + 1).fill(0); // sold[t]: most cash after t complete trades
  for (const p of prices) {
    for (let t = 1; t <= k; t++) {
      bought[t] = Math.max(bought[t], sold[t - 1] - p);
      sold[t] = Math.max(sold[t], bought[t] + p);
    }
  }
  return sold[k];
}

const prices = [3, 8, 5, 1, 7, 8];
const zigzag = [2, 5, 1, 4, 3, 6];
assert.equal(bestProfit(prices, 2), 12);
assert.equal(bestProfit(prices, 1), 7);
assert.equal(bestProfit([9, 7, 4, 1], 2), 0);
assert.equal(bestProfit(prices, 0), 0);
assert.equal(bestProfit([], 3), 0);
assert.equal(bestProfit([7], 1), 0);
assert.equal(bestProfit([1, 2, 3, 4, 5], 2), 4);
assert.equal(bestProfit(zigzag, 1), 5);
assert.equal(bestProfit(zigzag, 2), 8);
assert.equal(bestProfit(zigzag, 3), 9);
assert.equal(bestProfit(zigzag, 10), 9);
assert.equal(bestProfit([1, 3, 2, 8], 1), 7);
console.log("179 ok");
