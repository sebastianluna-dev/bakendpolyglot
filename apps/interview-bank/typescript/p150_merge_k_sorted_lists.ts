// 150 · Merge k Sorted Lists
import assert from "node:assert/strict";

class MinHeap<T> {
  items: T[] = [];
  less: (a: T, b: T) => boolean;

  constructor(less: (a: T, b: T) => boolean) {
    this.less = less;
  }

  get size(): number {
    return this.items.length;
  }

  peek(): T | undefined {
    return this.items[0];
  }

  push(item: T): void {
    const a = this.items;
    a.push(item);
    let i = a.length - 1;
    while (i > 0) {
      const parent = (i - 1) >> 1;
      if (!this.less(a[i], a[parent])) break;
      [a[i], a[parent]] = [a[parent], a[i]];
      i = parent;
    }
  }

  pop(): T | undefined {
    const a = this.items;
    if (a.length === 0) return undefined;
    const top = a[0];
    const end = a.pop()!;
    if (a.length > 0) {
      a[0] = end;
      let i = 0;
      while (true) {
        const left = 2 * i + 1;
        const right = left + 1;
        let smallest = i;
        if (left < a.length && this.less(a[left], a[smallest])) smallest = left;
        if (right < a.length && this.less(a[right], a[smallest])) smallest = right;
        if (smallest === i) break;
        [a[i], a[smallest]] = [a[smallest], a[i]];
        i = smallest;
      }
    }
    return top;
  }
}

export class ListNode {
  val: number;
  next: ListNode | null;

  constructor(val: number, next: ListNode | null = null) {
    this.val = val;
    this.next = next;
  }
}

export function mergeLists(lists: (ListNode | null)[]): ListNode | null {
  const heap = new MinHeap<ListNode>((a, b) => a.val < b.val); // the front node of each list
  for (const head of lists) if (head !== null) heap.push(head);
  const dummy = new ListNode(0);
  let tail = dummy;
  while (heap.size > 0) {
    const node = heap.pop()!;
    tail.next = node;
    tail = node;
    if (node.next !== null) heap.push(node.next);
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

const merge = (lists: number[][]) => toArray(mergeLists(lists.map(fromArray)));
assert.deepEqual(merge([[1, 4, 7], [2, 5], [0, 6, 9]]), [0, 1, 2, 4, 5, 6, 7, 9]);
assert.deepEqual(merge([[], [3], []]), [3]);
assert.deepEqual(merge([]), []);
assert.deepEqual(merge([[]]), []);
assert.deepEqual(merge([[2, 2], [2], [1, 2]]), [1, 2, 2, 2, 2]);
assert.deepEqual(merge([[-5, 0, 5]]), [-5, 0, 5]);
assert.deepEqual(merge([[10], [-10], [0], [-20, 20]]), [-20, -10, 0, 10, 20]);
console.log("150 ok");
