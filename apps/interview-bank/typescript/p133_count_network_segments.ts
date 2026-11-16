// 133 · Count Network Segments
import assert from "node:assert/strict";

export function countSegments(n: number, links: number[][]): number {
  const parent = Array.from({ length: n }, (_, i) => i);
  const size = new Array<number>(n).fill(1);
  let segments = n;
  for (const [a, b] of links) {
    let ra = find(parent, a);
    let rb = find(parent, b);
    if (ra === rb) continue;
    if (size[ra] < size[rb]) [ra, rb] = [rb, ra];
    parent[rb] = ra; // the smaller tree goes under the larger one
    size[ra] += size[rb];
    segments--;
  }
  return segments;
}

function find(parent: number[], x: number): number {
  while (parent[x] !== x) {
    parent[x] = parent[parent[x]]; // path halving: point x at its grandparent
    x = parent[x];
  }
  return x;
}

assert.equal(countSegments(5, [[0, 1], [1, 2], [3, 4]]), 2);
assert.equal(countSegments(6, [[0, 1], [2, 3], [1, 0]]), 4);
assert.equal(countSegments(1, []), 1);
assert.equal(countSegments(3, []), 3);
assert.equal(countSegments(3, [[0, 1], [1, 2], [2, 0]]), 1);
assert.equal(countSegments(2, [[1, 1]]), 2);
assert.equal(countSegments(6, [[0, 1], [2, 3], [4, 5], [1, 3], [5, 0]]), 1);
console.log("133 ok");
