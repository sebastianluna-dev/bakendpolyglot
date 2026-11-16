// 082 · Merge Two Sorted Lists
import assert from "node:assert/strict";

export class ListNode {
  val: number;
  next: ListNode | null;

  constructor(val: number, next: ListNode | null = null) {
    this.val = val;
    this.next = next;
  }
}

export function mergeSorted(a: ListNode | null, b: ListNode | null): ListNode | null {
  const dummy = new ListNode(0);
  let tail = dummy;
  while (a !== null && b !== null) {
    if (a.val <= b.val) {
      tail.next = a;
      tail = a;
      a = a.next;
    } else {
      tail.next = b;
      tail = b;
      b = b.next;
    }
  }
  tail.next = a ?? b; // whatever is left is already sorted
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

const merge = (a: number[], b: number[]) => toArray(mergeSorted(fromArray(a), fromArray(b)));
assert.deepEqual(merge([1, 4, 6], [2, 3, 6, 9]), [1, 2, 3, 4, 6, 6, 9]);
assert.deepEqual(merge([], [5, 8]), [5, 8]);
assert.deepEqual(merge([], []), []);
assert.deepEqual(merge([7, 8], [1, 2]), [1, 2, 7, 8]);
assert.deepEqual(merge([-3, 0], [-3]), [-3, -3, 0]);
const first = fromArray([2]);
const second = fromArray([2]);
const merged = mergeSorted(first, second);
assert.ok(merged === first && merged?.next === second);
console.log("082 ok");
