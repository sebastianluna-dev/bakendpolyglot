// 119 · Count Downward Paths With a Given Sum
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

export function countPathsWithSum(root: TreeNode | null, target: number): number {
  const seen = new Map<number, number>([[0, 1]]); // prefix sums on the current root path -> count
  const visit = (node: TreeNode | null, prefix: number): number => {
    if (node === null) return 0;
    const total = prefix + node.val;
    let count = seen.get(total - target) ?? 0;
    seen.set(total, (seen.get(total) ?? 0) + 1);
    count += visit(node.left, total) + visit(node.right, total);
    seen.set(total, seen.get(total)! - 1);
    return count;
  };
  return visit(root, 0);
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

assert.equal(countPathsWithSum(fromLevelOrder([4, 2, -1, 1, 3, null, 5]), 5), 2);
assert.equal(countPathsWithSum(fromLevelOrder([1, -1, 1, 1]), 1), 4);
assert.equal(countPathsWithSum(fromLevelOrder([]), 0), 0);
assert.equal(countPathsWithSum(fromLevelOrder([0, 0, null, 0]), 0), 6);
assert.equal(countPathsWithSum(fromLevelOrder([1000000000, null, 1000000000]), 2000000000), 1);
assert.equal(countPathsWithSum(fromLevelOrder([1000000000, null, 1000000000]), -294967296), 0);
console.log("119 ok");
