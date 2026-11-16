// 083 · Does the List Loop?
import assert from "node:assert/strict";

export class ListNode {
  val: number;
  next: ListNode | null;

  constructor(val: number, next: ListNode | null = null) {
    this.val = val;
    this.next = next;
  }
}

export function hasLoop(head: ListNode | null): boolean {
  let slow = head;
  let fast = head;
  while (fast !== null && fast.next !== null) {
    slow = slow!.next; // slow is behind fast, so it is not null here
    fast = fast.next.next;
    if (slow === fast) return true;
  }
  return false;
}

// Builds the list and links its last node to the node at index loopTo (-1: no loop).
function withLoop(values: number[], loopTo: number): ListNode | null {
  const nodes = values.map((v) => new ListNode(v));
  for (let i = 0; i + 1 < nodes.length; i++) nodes[i].next = nodes[i + 1];
  if (loopTo >= 0) nodes[nodes.length - 1].next = nodes[loopTo];
  return nodes.length > 0 ? nodes[0] : null;
}

assert.equal(hasLoop(withLoop([3, 8, 1, 6], 1)), true);
assert.equal(hasLoop(withLoop([4, 2], -1)), false);
assert.equal(hasLoop(withLoop([5], 0)), true);
assert.equal(hasLoop(withLoop([], -1)), false);
assert.equal(hasLoop(withLoop([2, 5, 2], -1)), false);
assert.equal(hasLoop(withLoop([1, 2, 3, 4, 5, 6], 0)), true);
assert.equal(hasLoop(withLoop([7, 1], 1)), true);
console.log("083 ok");
