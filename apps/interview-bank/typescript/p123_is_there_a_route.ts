// 123 · Is There a Route?
import assert from "node:assert/strict";

export function hasRoute(n: number, edges: number[][], source: number, target: number): boolean {
  const graph: number[][] = Array.from({ length: n }, () => []);
  for (const [a, b] of edges) {
    graph[a].push(b);
    graph[b].push(a);
  }
  const seen = new Array<boolean>(n).fill(false);
  seen[source] = true;
  const queue: number[] = [source];
  let head = 0;
  while (head < queue.length) {
    const node = queue[head++];
    if (node === target) return true;
    for (const next of graph[node]) {
      if (!seen[next]) {
        seen[next] = true; // mark when queued, so no node is queued twice
        queue.push(next);
      }
    }
  }
  return false;
}

const roads = [[0, 1], [1, 3], [3, 2], [4, 5]];
assert.equal(hasRoute(6, roads, 0, 2), true);
assert.equal(hasRoute(6, roads, 0, 5), false);
assert.equal(hasRoute(1, [], 0, 0), true);
assert.equal(hasRoute(3, [], 0, 2), false);
assert.equal(hasRoute(2, [[1, 0]], 0, 1), true);
assert.equal(hasRoute(4, [[0, 1], [1, 0], [1, 2], [2, 0], [2, 3]], 3, 0), true);
console.log("123 ok");
