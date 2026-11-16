// 081 · Reverse a Linked List
import assert from "node:assert/strict";

export class ListNode {
  val: number;
  next: ListNode | null;

  constructor(val: number, next: ListNode | null = null) {
    this.val = val;
    this.next = next;
  }
}

export function reverseList(head: ListNode | null): ListNode | null {
  let prev: ListNode | null = null;
  let node = head;
  while (node !== null) {
    const next = node.next; // save it before the link is overwritten
    node.next = prev;
    prev = node;
    node = next;
  }
  return prev;
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

assert.deepEqual(toArray(reverseList(fromArray([4, 8, 15, 16, 23]))), [23, 16, 15, 8, 4]);
assert.deepEqual(toArray(reverseList(fromArray([2, 9]))), [9, 2]);
assert.deepEqual(toArray(reverseList(fromArray([7]))), [7]);
assert.equal(reverseList(fromArray([])), null);
assert.deepEqual(toArray(reverseList(fromArray([5, 5, 1]))), [1, 5, 5]);
console.log("081 ok");
