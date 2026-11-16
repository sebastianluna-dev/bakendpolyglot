// 115 · Lowest Common Manager
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

export function lowestCommonManager(root: TreeNode | null, a: number, b: number): number {
  return lowest(root, a, b)!.val;
}

// returns the answer if both ids are below node, else whichever of the two is below it, else null
function lowest(node: TreeNode | null, a: number, b: number): TreeNode | null {
  if (node === null || node.val === a || node.val === b) return node;
  const left = lowest(node.left, a, b);
  const right = lowest(node.right, a, b);
  if (left !== null && right !== null) return node;
  return left ?? right;
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

const chart = [1, 2, 3, 4, 5, 6, 7, null, null, 8, 9];
assert.equal(lowestCommonManager(fromLevelOrder(chart), 8, 4), 2);
assert.equal(lowestCommonManager(fromLevelOrder(chart), 9, 7), 1);
assert.equal(lowestCommonManager(fromLevelOrder(chart), 5, 9), 5);
assert.equal(lowestCommonManager(fromLevelOrder(chart), 6, 6), 6);
assert.equal(lowestCommonManager(fromLevelOrder(chart), 8, 9), 5);
assert.equal(lowestCommonManager(fromLevelOrder([1, 2]), 2, 1), 1);
console.log("115 ok");
