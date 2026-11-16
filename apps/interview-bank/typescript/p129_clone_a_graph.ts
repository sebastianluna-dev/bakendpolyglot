// 129 · Clone a Graph
import assert from "node:assert/strict";

export class GraphNode {
  val: number;
  neighbors: GraphNode[] = [];

  constructor(val: number) {
    this.val = val;
  }
}

export function cloneGraph(node: GraphNode | null): GraphNode | null {
  if (node === null) return null;
  const copyOf = new Map<GraphNode, GraphNode>([[node, new GraphNode(node.val)]]); // original -> copy
  const queue: GraphNode[] = [node];
  let head = 0;
  while (head < queue.length) {
    const current = queue[head++];
    for (const nb of current.neighbors) {
      if (!copyOf.has(nb)) {
        copyOf.set(nb, new GraphNode(nb.val));
        queue.push(nb);
      }
      copyOf.get(current)!.neighbors.push(copyOf.get(nb)!);
    }
  }
  return copyOf.get(node)!;
}

// Node i has value i and the neighbours adjacency[i]; returns node 0.
function build(adjacency: number[][]): GraphNode | null {
  const nodes = adjacency.map((_, i) => new GraphNode(i));
  adjacency.forEach((neighbors, i) => {
    nodes[i].neighbors = neighbors.map((j) => nodes[j]);
  });
  return nodes.length > 0 ? nodes[0] : null;
}

function allNodes(node: GraphNode | null): Set<GraphNode> {
  const found = new Set<GraphNode>();
  if (node === null) return found;
  found.add(node);
  const queue: GraphNode[] = [node];
  let head = 0;
  while (head < queue.length) {
    for (const nb of queue[head++].neighbors) {
      if (!found.has(nb)) {
        found.add(nb);
        queue.push(nb);
      }
    }
  }
  return found;
}

function toAdjacency(node: GraphNode | null): number[][] {
  const nodes = allNodes(node);
  const adjacency: number[][] = Array.from({ length: nodes.size }, () => []);
  for (const n of nodes) adjacency[n.val] = n.neighbors.map((nb) => nb.val);
  return adjacency;
}

function checkClone(adjacency: number[][]): void {
  const original = build(adjacency);
  const copy = cloneGraph(original);
  assert.deepEqual(toAdjacency(copy), adjacency);
  const originals = allNodes(original);
  for (const n of allNodes(copy)) assert.ok(!originals.has(n));
}

checkClone([[1, 3], [0, 2], [1, 3], [0, 2]]);
checkClone([[]]);
checkClone([]);
checkClone([[1], [0]]);
checkClone([[1, 2], [0, 2], [0, 1, 3], [2]]);
console.log("129 ok");
