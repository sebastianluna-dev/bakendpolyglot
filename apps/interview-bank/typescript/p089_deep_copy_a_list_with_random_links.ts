// 089 · Deep-Copy a List With Random Links
import assert from "node:assert/strict";

export class ListNode {
  val: number;
  next: ListNode | null;
  random: ListNode | null = null;

  constructor(val: number, next: ListNode | null = null) {
    this.val = val;
    this.next = next;
  }
}

export function deepCopy(head: ListNode | null): ListNode | null {
  const copyOf = new Map<ListNode, ListNode>(); // original node -> its copy
  for (let node = head; node !== null; node = node.next) copyOf.set(node, new ListNode(node.val));
  for (let node = head; node !== null; node = node.next) {
    const copy = copyOf.get(node)!;
    copy.next = node.next === null ? null : copyOf.get(node.next)!;
    copy.random = node.random === null ? null : copyOf.get(node.random)!;
  }
  return head === null ? null : copyOf.get(head)!;
}

// Each pair is [value, index of the random target], with -1 for no target.
function fromPairs(pairs: [number, number][]): ListNode | null {
  const nodes = pairs.map(([val]) => new ListNode(val));
  for (let i = 0; i < nodes.length; i++) {
    nodes[i].next = i + 1 < nodes.length ? nodes[i + 1] : null;
    if (pairs[i][1] >= 0) nodes[i].random = nodes[pairs[i][1]];
  }
  return nodes.length > 0 ? nodes[0] : null;
}

function toPairs(head: ListNode | null): [number, number][] {
  const index = new Map<ListNode, number>();
  let i = 0;
  for (let node = head; node !== null; node = node.next) index.set(node, i++);
  const pairs: [number, number][] = [];
  for (let node = head; node !== null; node = node.next) {
    pairs.push([node.val, node.random === null ? -1 : index.get(node.random)!]);
  }
  return pairs;
}

function sharesNode(a: ListNode | null, b: ListNode | null): boolean {
  const seen = new Set<ListNode>();
  for (let node = a; node !== null; node = node.next) seen.add(node);
  for (let node = b; node !== null; node = node.next) {
    if (seen.has(node) || (node.random !== null && seen.has(node.random))) return true;
  }
  return false;
}

function checkCopy(pairs: [number, number][]): void {
  const original = fromPairs(pairs);
  const copy = deepCopy(original);
  assert.deepEqual(toPairs(copy), pairs);
  assert.deepEqual(toPairs(original), pairs);
  assert.equal(sharesNode(original, copy), false);
}

checkCopy([[4, 2], [9, 0], [6, -1]]);
checkCopy([[1, 1], [2, 1]]);
checkCopy([]);
checkCopy([[5, 0]]);
checkCopy([[7, 1], [7, 0]]);
checkCopy([[3, -1], [8, -1], [2, -1]]);
console.log("089 ok");
