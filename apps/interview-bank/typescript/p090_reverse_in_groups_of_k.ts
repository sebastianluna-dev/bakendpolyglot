// 090 · Reverse in Groups of k
import assert from "node:assert/strict";

export class ListNode {
  val: number;
  next: ListNode | null;

  constructor(val: number, next: ListNode | null = null) {
    this.val = val;
    this.next = next;
  }
}

export function reverseInGroups(head: ListNode | null, k: number): ListNode | null {
  const dummy = new ListNode(0, head);
  let before = dummy; // the node just before the next group
  while (true) {
    let last: ListNode | null = before;
    for (let i = 0; i < k && last !== null; i++) last = last.next;
    if (last === null) return dummy.next; // fewer than k nodes left: keep them as they are
    const after = last.next;
    const first = before.next!;
    let prev = after;
    let node: ListNode | null = first;
    while (node !== after) {
      const next: ListNode | null = node!.next;
      node!.next = prev;
      prev = node;
      node = next;
    }
    before.next = last;
    before = first;
  }
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

const groups = (values: number[], k: number) => toArray(reverseInGroups(fromArray(values), k));
assert.deepEqual(groups([1, 2, 3, 4, 5, 6, 7, 8], 3), [3, 2, 1, 6, 5, 4, 7, 8]);
assert.deepEqual(groups([1, 2, 3, 4, 5, 6], 2), [2, 1, 4, 3, 6, 5]);
assert.deepEqual(groups([4, 5, 6], 1), [4, 5, 6]);
assert.deepEqual(groups([4, 5, 6], 3), [6, 5, 4]);
assert.deepEqual(groups([4, 5, 6], 4), [4, 5, 6]);
assert.deepEqual(groups([], 2), []);
const head = fromArray([1, 2, 3]);
assert.ok(reverseInGroups(head, 3)?.next?.next === head);
console.log("090 ok");
