// 105 · Are Two Trees Identical?
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

export function areIdentical(a: TreeNode | null, b: TreeNode | null): boolean {
  if (a === null || b === null) return a === b;
  return a.val === b.val && areIdentical(a.left, b.left) && areIdentical(a.right, b.right);
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

assert.equal(areIdentical(fromLevelOrder([4, 2, 6]), fromLevelOrder([4, 2, 6])), true);
assert.equal(areIdentical(fromLevelOrder([4, 2]), fromLevelOrder([4, null, 2])), false);
assert.equal(areIdentical(fromLevelOrder([]), fromLevelOrder([])), true);
assert.equal(areIdentical(fromLevelOrder([1]), fromLevelOrder([])), false);
assert.equal(areIdentical(fromLevelOrder([1, 2, 3]), fromLevelOrder([1, 2, 4])), false);
assert.equal(areIdentical(fromLevelOrder([5, null, 8, 7]), fromLevelOrder([5, null, 8, 7])), true);
console.log("105 ok");
