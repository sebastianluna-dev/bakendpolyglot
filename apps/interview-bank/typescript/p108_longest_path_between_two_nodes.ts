// 108 · Longest Path Between Two Nodes
import assert from "node:assert/strict";

export class TreeNode {
  val: number;
  left: TreeNode | null;
  right: TreeNode | null;

  constructor(val: number, left: TreeNode | null = null, right: TreeNode | null = null) {
    this.val = val;
    this.left = left;
    this.right = right;
  }
}

export function longestPath(root: TreeNode | null): number {
  let best = 0;
  // Returns the depth in nodes, which is also the number of edges from the parent down.
  const depth = (node: TreeNode | null): number => {
    if (node === null) return 0;
    const left = depth(node.left);
    const right = depth(node.right);
    best = Math.max(best, left + right);
    return 1 + Math.max(left, right);
  };
  depth(root);
  return best;
}

function fromLevelOrder(values: (number | null)[]): TreeNode | null {
  if (values.length === 0 || values[0] === null) return null;
  const root = new TreeNode(values[0]);
  const queue = [root];
  let head = 0;
  for (let i = 1; i < values.length; i += 2) {
    const node = queue[head++];
    const left = values[i];
    const right = i + 1 < values.length ? values[i + 1] : null;
    if (left !== null) {
      node.left = new TreeNode(left);
      queue.push(node.left);
    }
    if (right !== null) {
      node.right = new TreeNode(right);
      queue.push(node.right);
    }
  }
  return root;
}

assert.equal(longestPath(fromLevelOrder([8, 4, 11, 2, 6, null, null, 1])), 4);
assert.equal(longestPath(fromLevelOrder([1, 2, null, 3, 4, 5, null, null, 6, 7, null, null, 8])), 6);
assert.equal(longestPath(fromLevelOrder([])), 0);
assert.equal(longestPath(fromLevelOrder([9])), 0);
assert.equal(longestPath(fromLevelOrder([1, 2])), 1);
console.log("108 ok");
