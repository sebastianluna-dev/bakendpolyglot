// 114 · The k-th Smallest Key in a BST
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

export function kthSmallest(root: TreeNode | null, k: number): number {
  const stack: TreeNode[] = [];
  let node = root;
  while (true) {
    while (node !== null) {
      stack.push(node);
      node = node.left;
    }
    const smallest = stack.pop()!; // the smallest key not yet counted
    k--;
    if (k === 0) return smallest.val;
    node = smallest.right;
  }
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

assert.equal(kthSmallest(fromLevelOrder([20, 10, 30, 5, 15, 25, 40]), 3), 15);
assert.equal(kthSmallest(fromLevelOrder([6, 3, 8, 1, 4, null, 9, null, 2]), 2), 2);
assert.equal(kthSmallest(fromLevelOrder([6, 3, 8, 1, 4, null, 9, null, 2]), 7), 9);
assert.equal(kthSmallest(fromLevelOrder([42]), 1), 42);
assert.equal(kthSmallest(fromLevelOrder([1, null, 2, null, 3]), 3), 3);
assert.equal(kthSmallest(fromLevelOrder([3, 2, null, 1]), 1), 1);
console.log("114 ok");
