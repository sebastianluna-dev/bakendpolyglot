// 121 · Maximum Path Sum
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

export function maxPathSum(root: TreeNode): number {
  let best = root.val;
  // returns the best sum of a path that starts at node and goes down, or 0 for no node
  const gain = (node: TreeNode | null): number => {
    if (node === null) return 0;
    const left = Math.max(0, gain(node.left)); // a negative branch is better left out
    const right = Math.max(0, gain(node.right));
    best = Math.max(best, node.val + left + right);
    return node.val + Math.max(left, right);
  };
  gain(root);
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

assert.equal(maxPathSum(fromLevelOrder([6, -8, 3, null, 9, -1, 4])!), 14);
assert.equal(maxPathSum(fromLevelOrder([-3, -1, -2])!), -1);
assert.equal(maxPathSum(fromLevelOrder([-5])!), -5);
assert.equal(maxPathSum(fromLevelOrder([-10, 4, -1, 6, 7])!), 17);
assert.equal(maxPathSum(fromLevelOrder([2, -1, null, 5])!), 6);
assert.equal(maxPathSum(fromLevelOrder([1, -2, 3, 4, 5, -6, 2])!), 9);
console.log("121 ok");
