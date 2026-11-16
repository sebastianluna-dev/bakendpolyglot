// 085 · Drop Repeated Values From a Sorted List
import assert from "node:assert/strict";

export class ListNode {
  val: number;
  next: ListNode | null;

  constructor(val: number, next: ListNode | null = null) {
    this.val = val;
    this.next = next;
  }
}

export function dropRepeats(head: ListNode | null): ListNode | null {
  let node = head;
  while (node !== null && node.next !== null) {
    if (node.next.val === node.val) node.next = node.next.next; // stay: the new next may repeat too
    else node = node.next;
  }
  return head;
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

const drop = (values: number[]) => toArray(dropRepeats(fromArray(values)));
assert.deepEqual(drop([1, 1, 2, 3, 3, 3]), [1, 2, 3]);
assert.deepEqual(drop([4, 4, 4]), [4]);
assert.deepEqual(drop([]), []);
assert.deepEqual(drop([1, 2, 3]), [1, 2, 3]);
assert.deepEqual(drop([-2, -2, 0, 5, 5]), [-2, 0, 5]);
assert.deepEqual(drop([8]), [8]);
console.log("085 ok");
