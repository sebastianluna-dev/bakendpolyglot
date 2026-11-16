// 084 · The Middle Node
import assert from "node:assert/strict";

export class ListNode {
  val: number;
  next: ListNode | null;

  constructor(val: number, next: ListNode | null = null) {
    this.val = val;
    this.next = next;
  }
}

export function middleNode(head: ListNode | null): ListNode | null {
  let slow = head;
  let fast = head;
  while (fast !== null && fast.next !== null) {
    slow = slow!.next; // slow is behind fast, so it is not null here
    fast = fast.next.next;
  }
  return slow;
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

const fromMiddle = (values: number[]) => toArray(middleNode(fromArray(values)));
assert.deepEqual(fromMiddle([3, 1, 4, 1, 5]), [4, 1, 5]);
assert.deepEqual(fromMiddle([2, 7, 1, 8]), [1, 8]);
assert.deepEqual(fromMiddle([6]), [6]);
assert.equal(middleNode(null), null);
assert.deepEqual(fromMiddle([9, 4]), [4]);
const head = fromArray([10, 20, 30]);
assert.ok(middleNode(head) === head?.next);
console.log("084 ok");
