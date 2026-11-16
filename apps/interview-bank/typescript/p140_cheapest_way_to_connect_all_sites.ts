// 140 · Cheapest Way to Connect All Sites
import assert from "node:assert/strict";

export function cheapestConnection(n: number, cables: number[][]): number {
  const sorted = [...cables].sort((a, b) => a[2] - b[2]);
  const parent = Array.from({ length: n }, (_, i) => i);
  const size = new Array<number>(n).fill(1);
  let total = 0;
  let used = 0;
  for (const [a, b, cost] of sorted) {
    let ra = find(parent, a);
    let rb = find(parent, b);
    if (ra === rb) continue; // both ends already connected: this cable would close a loop
    if (size[ra] < size[rb]) [ra, rb] = [rb, ra];
    parent[rb] = ra;
    size[ra] += size[rb];
    total += cost;
    used++;
  }
  return used === n - 1 ? total : -1;
}

function find(parent: number[], x: number): number {
  while (parent[x] !== x) {
    parent[x] = parent[parent[x]]; // path halving: point x at its grandparent
    x = parent[x];
  }
  return x;
}

assert.equal(cheapestConnection(4, [[0, 1, 3], [1, 2, 1], [2, 3, 4], [0, 2, 2], [1, 3, 5]]), 7);
assert.equal(cheapestConnection(3, [[0, 1, 5]]), -1);
assert.equal(cheapestConnection(1, []), 0);
assert.equal(cheapestConnection(2, []), -1);
assert.equal(cheapestConnection(2, [[0, 1, 9], [1, 0, 4], [0, 0, 1]]), 4);
assert.equal(cheapestConnection(5, [[0, 1, 2], [1, 2, 2], [2, 0, 2], [3, 4, 1], [2, 3, 7], [0, 4, 8]]), 12);
console.log("140 ok");
