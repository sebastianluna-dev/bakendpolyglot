// 086 · Remove the k-th Node From the End
import assert from "node:assert/strict";

export class ListNode {
  val: number;
  next: ListNode | null;

  constructor(val: number, next: ListNode | null = null) {
    this.val = val;
    this.next = next;
  }
}

export function removeKthFromEnd(head: ListNode | null, k: number): ListNode | null {
  const dummy = new ListNode(0, head); // the node before the head, in case the head goes
  let lead = head;
  for (let i = 0; i < k; i++) lead = lead!.next;
  let trail = dummy;
  while (lead !== null) {
    lead = lead.next;
    trail = trail.next!;
  }
  trail.next = trail.next!.next;
  return dummy.next;
}

function fromArray(values: number[]): ListNode | null {
  let head: ListNode | null = null;
  for (let i = values.length - 1; i >= 0; i--) head = new ListNode(values[i], head);
  return head;
}

function toArray(head: ListNode | null): number[] {
  const values: number[] = [];
  for (let node = head; node !== null; node = node.next) values.push(node.val);
  return values;
}

const remove = (values: number[], k: number) => toArray(removeKthFromEnd(fromArray(values), k));
assert.deepEqual(remove([10, 20, 30, 40, 50], 2), [10, 20, 30, 50]);
assert.deepEqual(remove([8], 1), []);
assert.deepEqual(remove([1, 2], 2), [2]);
assert.deepEqual(remove([1, 2], 1), [1]);
assert.deepEqual(remove([5, 6, 7], 3), [6, 7]);
assert.deepEqual(remove([5, 6, 7], 1), [5, 6]);
console.log("086 ok");
