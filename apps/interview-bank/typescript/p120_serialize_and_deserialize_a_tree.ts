// 120 · Serialize and Deserialize a Tree
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

export function serialize(root: TreeNode | null): string {
  const tokens: string[] = [];
  const write = (node: TreeNode | null): void => {
    if (node === null) {
      tokens.push("#");
      return;
    }
    tokens.push(String(node.val));
    write(node.left);
    write(node.right);
  };
  write(root);
  return tokens.join(",");
}

export function deserialize(data: string): TreeNode | null {
  const tokens = data.split(",");
  let next = 0; // index of the next token to read
  const read = (): TreeNode | null => {
    const token = tokens[next++];
    if (token === "#") return null;
    const node = new TreeNode(Number(token));
    node.left = read();
    node.right = read();
    return node;
  };
  return read();
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

assert.equal(serialize(fromLevelOrder([7, 3, 9, null, 4, 8])), "7,3,#,4,#,#,9,8,#,#,#");
assert.equal(serialize(fromLevelOrder([])), "#");
assert.deepEqual(toLevelOrder(deserialize("5,-2,#,#,#")), [5, -2]);
const trees: (number | null)[][] = [[7, 3, 9, null, 4, 8], [], [-1000], [1, null, 2, null, 3], [10, -20, 30, -40, 50, -60, 70]];
for (const tree of trees) {
  assert.deepEqual(toLevelOrder(deserialize(serialize(fromLevelOrder(tree)))), tree);
}
console.log("120 ok");
