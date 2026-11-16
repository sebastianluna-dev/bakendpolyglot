// 110 · Sum of a Key Range in a BST
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

export function rangeSum(root: TreeNode | null, lo: number, hi: number): number {
  if (root === null) return 0;
  if (root.val < lo) return rangeSum(root.right, lo, hi); // the left subtree is even smaller
  if (root.val > hi) return rangeSum(root.left, lo, hi);
  return root.val + rangeSum(root.left, lo, hi) + rangeSum(root.right, lo, hi);
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

assert.equal(rangeSum(fromLevelOrder([20, 10, 30, 5, 15, 25, 40]), 12, 27), 60);
assert.equal(rangeSum(fromLevelOrder([8, 3, 12, null, 6]), 9, 11), 0);
assert.equal(rangeSum(fromLevelOrder([]), 1, 100), 0);
assert.equal(rangeSum(fromLevelOrder([7]), 7, 7), 7);
assert.equal(rangeSum(fromLevelOrder([20, 10, 30, 5, 15, 25, 40]), 1, 100), 145);
assert.equal(rangeSum(fromLevelOrder([20, 10, 30, 5, 15, 25, 40]), 10, 20), 45);
console.log("110 ok");
