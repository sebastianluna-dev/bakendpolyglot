// 109 · A Root-to-Leaf Path With a Given Sum
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

export function hasPathSum(root: TreeNode | null, target: number): boolean {
  if (root === null) return false;
  if (root.left === null && root.right === null) return root.val === target;
  const rest = target - root.val;
  return hasPathSum(root.left, rest) || hasPathSum(root.right, rest);
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

assert.equal(hasPathSum(fromLevelOrder([5, 3, 8, 1, null, null, 4]), 9), true);
assert.equal(hasPathSum(fromLevelOrder([2, 7]), 2), false);
assert.equal(hasPathSum(fromLevelOrder([]), 0), false);
assert.equal(hasPathSum(fromLevelOrder([5, 3, 8, 1, null, null, 4]), 17), true);
assert.equal(hasPathSum(fromLevelOrder([5, 3, 8, 1, null, null, 4]), 8), false);
assert.equal(hasPathSum(fromLevelOrder([-3, 1, -2]), -5), true);
console.log("109 ok");
