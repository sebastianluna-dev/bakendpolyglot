// 111 · Level-by-Level Traversal
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

export function levelOrder(root: TreeNode | null): number[][] {
  const levels: number[][] = [];
  let level = root === null ? [] : [root];
  while (level.length > 0) {
    const next: TreeNode[] = [];
    levels.push(level.map((node) => node.val));
    for (const node of level) {
      if (node.left !== null) next.push(node.left);
      if (node.right !== null) next.push(node.right);
    }
    level = next;
  }
  return levels;
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

assert.deepEqual(levelOrder(fromLevelOrder([8, 4, 12, 2, null, 10, 14])), [[8], [4, 12], [2, 10, 14]]);
assert.deepEqual(levelOrder(fromLevelOrder([5, 6, null, 7, 8])), [[5], [6], [7, 8]]);
assert.deepEqual(levelOrder(fromLevelOrder([])), []);
assert.deepEqual(levelOrder(fromLevelOrder([9])), [[9]]);
assert.deepEqual(levelOrder(fromLevelOrder([1, 2, 3, null, 4, 5, null, null, null, 6])), [[1], [2, 3], [4, 5], [6]]);
console.log("111 ok");
