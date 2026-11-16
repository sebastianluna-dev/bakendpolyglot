// 104 · Mirror a Tree
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

export function mirrorTree(root: TreeNode | null): TreeNode | null {
  if (root === null) return null;
  const left = root.left; // keep it: the next line overwrites root.left
  root.left = mirrorTree(root.right);
  root.right = mirrorTree(left);
  return root;
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

assert.deepEqual(toLevelOrder(mirrorTree(fromLevelOrder([6, 2, 8, 1, 4, null, 9]))), [6, 8, 2, 9, null, 4, 1]);
assert.deepEqual(toLevelOrder(mirrorTree(fromLevelOrder([3, 1]))), [3, null, 1]);
assert.deepEqual(toLevelOrder(mirrorTree(fromLevelOrder([]))), []);
assert.deepEqual(toLevelOrder(mirrorTree(fromLevelOrder([7]))), [7]);
assert.deepEqual(toLevelOrder(mirrorTree(fromLevelOrder([1, 2, null, 3]))), [1, null, 2, null, 3]);
console.log("104 ok");
