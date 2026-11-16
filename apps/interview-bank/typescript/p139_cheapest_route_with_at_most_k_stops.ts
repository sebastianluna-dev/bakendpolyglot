// 139 · Cheapest Route With at Most k Stops
import assert from "node:assert/strict";

export function cheapestRoute(n: number, legs: number[][], source: number, target: number, k: number): number {
  let cost = new Array<number>(n).fill(Infinity);
  cost[source] = 0;
  for (let round = 0; round <= k; round++) {
    const next = [...cost]; // relax from last round's costs only, so each round adds at most one leg
    for (const [from, to, price] of legs) {
      if (cost[from] !== Infinity && cost[from] + price < next[to]) next[to] = cost[from] + price;
    }
    cost = next;
  }
  return cost[target] === Infinity ? -1 : cost[target];
}

const legs = [[0, 1, 100], [1, 2, 100], [2, 3, 100], [0, 2, 500], [1, 3, 600]];
const triangle = [[0, 1, 1], [1, 2, 1], [0, 2, 5]];
assert.equal(cheapestRoute(4, legs, 0, 3, 1), 600);
assert.equal(cheapestRoute(4, legs, 0, 3, 2), 300);
assert.equal(cheapestRoute(4, legs, 0, 3, 0), -1);
assert.equal(cheapestRoute(4, legs, 0, 3, 10), 300);
assert.equal(cheapestRoute(3, triangle, 0, 2, 0), 5);
assert.equal(cheapestRoute(3, triangle, 0, 2, 1), 2);
assert.equal(cheapestRoute(2, [[0, 1, 7]], 1, 1, 0), 0);
assert.equal(cheapestRoute(3, [[0, 1, 4]], 0, 2, 5), -1);
assert.equal(cheapestRoute(2, [[1, 0, 3]], 0, 1, 3), -1);
console.log("139 ok");
