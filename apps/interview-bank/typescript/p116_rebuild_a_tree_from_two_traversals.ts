// 116 · Rebuild a Tree From Two Traversals
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

export function buildTree(preorder: number[], inorder: number[]): TreeNode | null {
  const position = new Map<number, number>();
  inorder.forEach((value, i) => position.set(value, i));

  // builds the subtree whose preorder starts at pre and whose inorder is inorder[lo..hi]
  const build = (pre: number, lo: number, hi: number): TreeNode | null => {
    if (lo > hi) return null;
    const root = new TreeNode(preorder[pre]);
    const mid = position.get(root.val)!;
    root.left = build(pre + 1, lo, mid - 1);
    root.right = build(pre + 1 + (mid - lo), mid + 1, hi);
    return root;
  };
  return build(0, 0, inorder.length - 1);
}

function toLevelOrder(root: TreeNode | null): (number | null)[] {
  if (root === null) return [];
  const values: (number | null)[] = [root.val];
  const queue = [root];
  let head = 0;
  while (head < queue.length) {
    const node = queue[head++];
    for (const child of [node.left, node.right]) {
      if (child === null) {
        values.push(null);
      } else {
        values.push(child.val);
        queue.push(child);
      }
    }
  }
  while (values[values.length - 1] === null) values.pop();
  return values;
}

assert.deepEqual(toLevelOrder(buildTree([8, 4, 2, 6, 12, 14], [2, 4, 6, 8, 12, 14])), [8, 4, 12, 2, 6, null, 14]);
assert.deepEqual(toLevelOrder(buildTree([1, 2, 3], [3, 2, 1])), [1, 2, null, 3]);
assert.deepEqual(toLevelOrder(buildTree([], [])), []);
assert.deepEqual(toLevelOrder(buildTree([5], [5])), [5]);
assert.deepEqual(toLevelOrder(buildTree([1, 2, 3], [1, 2, 3])), [1, null, 2, null, 3]);
assert.deepEqual(toLevelOrder(buildTree([-1, -3, 0], [-3, -1, 0])), [-1, -3, 0]);
console.log("116 ok");
