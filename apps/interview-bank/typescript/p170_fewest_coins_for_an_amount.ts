// 170 · Fewest Coins for an Amount
import assert from "node:assert/strict";

export function fewestCoins(coins: number[], amount: number): number {
  const unreachable = amount + 1; // more coins than any real answer needs
  const fewest = new Array<number>(amount + 1).fill(unreachable);
  fewest[0] = 0;
  for (let a = 1; a <= amount; a++) {
    for (const coin of coins) {
      if (coin <= a) fewest[a] = Math.min(fewest[a], fewest[a - coin] + 1);
    }
  }
  return fewest[amount] === unreachable ? -1 : fewest[amount];
}

assert.equal(fewestCoins([1, 4, 6], 8), 2);
assert.equal(fewestCoins([5, 10], 3), -1);
assert.equal(fewestCoins([7], 0), 0);
assert.equal(fewestCoins([2], 3), -1);
assert.equal(fewestCoins([3, 7], 20), 4);
assert.equal(fewestCoins([2, 5, 10, 25], 99), 7);
assert.equal(fewestCoins([4, 7], 5000), 716);
console.log("170 ok");
