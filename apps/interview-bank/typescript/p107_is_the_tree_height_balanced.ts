// 107 · Is the Tree Height-Balanced?
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

export function isBalanced(root: TreeNode | null): boolean {
  return balancedHeight(root) !== -1;
}

// The height of the subtree, or -1 as soon as any subtree in it is unbalanced.
function balancedHeight(node: TreeNode | null): number {
  if (node === null) return 0;
  const left = balancedHeight(node.left);
  if (left === -1) return -1;
  const right = balancedHeight(node.right);
  if (right === -1 || Math.abs(left - right) > 1) return -1;
  return 1 + Math.max(left, right);
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

assert.equal(isBalanced(fromLevelOrder([10, 5, 15, null, null, 12, 20])), true);
assert.equal(isBalanced(fromLevelOrder([10, 5, null, 3, null, 1])), false);
assert.equal(isBalanced(fromLevelOrder([])), true);
assert.equal(isBalanced(fromLevelOrder([1, 2, 3, 4, null, null, 5, 6, null, null, 7])), false);
assert.equal(isBalanced(fromLevelOrder([1, 2, 3, 4])), true);
assert.equal(isBalanced(fromLevelOrder([1, 2])), true);
console.log("107 ok");
