// 106 · Is the Tree Symmetric?
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

export function isSymmetric(root: TreeNode | null): boolean {
  return root === null || isMirror(root.left, root.right);
}

function isMirror(a: TreeNode | null, b: TreeNode | null): boolean {
  if (a === null || b === null) return a === b;
  return a.val === b.val && isMirror(a.left, b.right) && isMirror(a.right, b.left);
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

assert.equal(isSymmetric(fromLevelOrder([7, 4, 4, 1, 9, 9, 1])), true);
assert.equal(isSymmetric(fromLevelOrder([5, 3, 3, 8, null, null, 2])), false);
assert.equal(isSymmetric(fromLevelOrder([])), true);
assert.equal(isSymmetric(fromLevelOrder([1])), true);
assert.equal(isSymmetric(fromLevelOrder([2, 3, 3, null, 4, null, 4])), false);
assert.equal(isSymmetric(fromLevelOrder([1, 2, 3])), false);
console.log("106 ok");
