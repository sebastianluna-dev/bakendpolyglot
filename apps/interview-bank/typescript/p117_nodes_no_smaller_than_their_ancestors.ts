// 117 · Nodes No Smaller Than Their Ancestors
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

export function countGoodNodes(root: TreeNode | null): number {
  return root === null ? 0 : countFrom(root, root.val);
}

// highest is the largest value on the path from the root down to node's parent
function countFrom(node: TreeNode | null, highest: number): number {
  if (node === null) return 0;
  const good = node.val >= highest ? 1 : 0;
  const next = Math.max(highest, node.val);
  return good + countFrom(node.left, next) + countFrom(node.right, next);
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

assert.equal(countGoodNodes(fromLevelOrder([5, 3, 8, 6, 2, 5, 9])), 4);
assert.equal(countGoodNodes(fromLevelOrder([2, 2, 2])), 3);
assert.equal(countGoodNodes(fromLevelOrder([])), 0);
assert.equal(countGoodNodes(fromLevelOrder([7])), 1);
assert.equal(countGoodNodes(fromLevelOrder([9, 8, null, 7])), 1);
assert.equal(countGoodNodes(fromLevelOrder([-5, -10, -1])), 2);
console.log("117 ok");
