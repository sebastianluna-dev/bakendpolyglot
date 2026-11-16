// 112 · What You See From the Right
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

export function rightSideView(root: TreeNode | null): number[] {
  const view: number[] = [];
  const visit = (node: TreeNode | null, depth: number): void => {
    if (node === null) return;
    if (depth === view.length) view.push(node.val); // first node reached at this depth
    visit(node.right, depth + 1);
    visit(node.left, depth + 1);
  };
  visit(root, 0);
  return view;
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

assert.deepEqual(rightSideView(fromLevelOrder([10, 4, 15, 2, 6, null, 20, null, null, 5])), [10, 15, 20, 5]);
assert.deepEqual(rightSideView(fromLevelOrder([1, 2])), [1, 2]);
assert.deepEqual(rightSideView(fromLevelOrder([])), []);
assert.deepEqual(rightSideView(fromLevelOrder([4])), [4]);
assert.deepEqual(rightSideView(fromLevelOrder([1, 2, null, 3])), [1, 2, 3]);
assert.deepEqual(rightSideView(fromLevelOrder([1, 2, 3, 4, 5, 6, 7])), [1, 3, 7]);
console.log("112 ok");
