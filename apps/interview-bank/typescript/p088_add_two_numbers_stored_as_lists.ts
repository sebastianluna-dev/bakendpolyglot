// 088 · Add Two Numbers Stored as Lists
import assert from "node:assert/strict";

export class ListNode {
  val: number;
  next: ListNode | null;

  constructor(val: number, next: ListNode | null = null) {
    this.val = val;
    this.next = next;
  }
}

export function addLists(a: ListNode | null, b: ListNode | null): ListNode | null {
  const dummy = new ListNode(0);
  let tail = dummy;
  let carry = 0;
  while (a !== null || b !== null || carry > 0) {
    const sum = carry + (a?.val ?? 0) + (b?.val ?? 0);
    carry = Math.floor(sum / 10);
    const digit = new ListNode(sum % 10);
    tail.next = digit;
    tail = digit;
    a = a?.next ?? null;
    b = b?.next ?? null;
  }
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

const add = (a: number[], b: number[]) => toArray(addLists(fromArray(a), fromArray(b)));
assert.deepEqual(add([5, 2, 9], [8, 4]), [3, 7, 9]);
assert.deepEqual(add([9, 9, 9], [1]), [0, 0, 0, 1]);
assert.deepEqual(add([0], [0]), [0]);
assert.deepEqual(add([1], [6, 0, 3]), [7, 0, 3]);
assert.deepEqual(add([9, 9], [9, 9]), [8, 9, 1]);
assert.deepEqual(add(Array(20).fill(9), [1]), [...Array(20).fill(0), 1]);
console.log("088 ok");
