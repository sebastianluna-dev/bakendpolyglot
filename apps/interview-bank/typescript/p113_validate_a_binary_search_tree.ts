// 113 · Validate a Binary Search Tree
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

export function isSearchTree(root: TreeNode | null): boolean {
  return within(root, null, null);
}

// lo and hi are the nearest ancestors the key must stay above and below; null means no bound
function within(node: TreeNode | null, lo: TreeNode | null, hi: TreeNode | null): boolean {
  if (node === null) return true;
  if (lo !== null && node.val <= lo.val) return false;
  if (hi !== null && node.val >= hi.val) return false;
  return within(node.left, lo, node) && within(node.right, node, hi);
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

const MIN = -(2 ** 31);
const MAX = 2 ** 31 - 1;
assert.equal(isSearchTree(fromLevelOrder([8, 4, 12, 2, 6, 10, 14])), true);
assert.equal(isSearchTree(fromLevelOrder([8, 4, 12, 2, 9, 10, 14])), false);
assert.equal(isSearchTree(fromLevelOrder([])), true);
assert.equal(isSearchTree(fromLevelOrder([5, 5])), false);
assert.equal(isSearchTree(fromLevelOrder([MIN, null, MAX])), true);
assert.equal(isSearchTree(fromLevelOrder([MAX, MAX])), false);
assert.equal(isSearchTree(fromLevelOrder([MIN, MIN])), false);
console.log("113 ok");
