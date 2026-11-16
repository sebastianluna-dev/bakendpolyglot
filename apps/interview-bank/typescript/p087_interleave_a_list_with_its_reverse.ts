// 087 · Interleave a List With Its Reverse
import assert from "node:assert/strict";

export class ListNode {
  val: number;
  next: ListNode | null;

  constructor(val: number, next: ListNode | null = null) {
    this.val = val;
    this.next = next;
  }
}

export function interleaveWithReverse(head: ListNode | null): void {
  if (head === null) return;
  let slow = head;
  let fast = head;
  while (fast.next !== null && fast.next.next !== null) {
    slow = slow.next!; // slow is behind fast, so it is not null here
    fast = fast.next.next;
  }
  let second = reverse(slow.next);
  slow.next = null; // cut the first half off
  let first: ListNode | null = head;
  while (first !== null && second !== null) {
    const nextFirst: ListNode | null = first.next;
    const nextSecond: ListNode | null = second.next;
    first.next = second;
    second.next = nextFirst;
    first = nextFirst;
    second = nextSecond;
  }
}

function reverse(head: ListNode | null): ListNode | null {
  let prev: ListNode | null = null;
  let node = head;
  while (node !== null) {
    const next = node.next;
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

function interleaved(values: number[]): number[] {
  const head = fromArray(values);
  interleaveWithReverse(head);
  return toArray(head);
}

assert.deepEqual(interleaved([1, 2, 3, 4, 5, 6]), [1, 6, 2, 5, 3, 4]);
assert.deepEqual(interleaved([3, 8, 5, 2, 9]), [3, 9, 8, 2, 5]);
assert.deepEqual(interleaved([]), []);
assert.deepEqual(interleaved([7]), [7]);
assert.deepEqual(interleaved([1, 2]), [1, 2]);
assert.deepEqual(interleaved([10, 20, 30]), [10, 30, 20]);
assert.deepEqual(interleaved([1, 2, 3, 4]), [1, 4, 2, 3]);
console.log("087 ok");
