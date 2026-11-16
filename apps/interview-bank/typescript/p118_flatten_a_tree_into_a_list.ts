// 118 · Flatten a Tree Into a List
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

export function flatten(root: TreeNode | null): void {
  let node = root;
  while (node !== null) {
    if (node.left !== null) {
      let tail = node.left; // the last node of the left subtree in preorder
      while (tail.right !== null) tail = tail.right;
      tail.right = node.right;
      node.right = node.left;
      node.left = null;
    }
    node = node.right;
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

function flattened(values: (number | null)[]): (number | null)[] {
  const root = fromLevelOrder(values);
  flatten(root);
  return toLevelOrder(root);
}

assert.deepEqual(flattened([10, 5, 20, 3, 7, null, 25]), [10, null, 5, null, 3, null, 7, null, 20, null, 25]);
assert.deepEqual(flattened([1, 2, null, 3]), [1, null, 2, null, 3]);
assert.deepEqual(flattened([]), []);
assert.deepEqual(flattened([8]), [8]);
assert.deepEqual(flattened([1, null, 2]), [1, null, 2]);
assert.deepEqual(flattened([1, 2, 3, null, null, 4]), [1, null, 2, null, 3, null, 4]);
console.log("118 ok");
