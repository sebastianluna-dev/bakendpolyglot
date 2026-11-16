// 141 · The k-th Largest in a Stream
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

export class KthLargest {
  k: number;
  heap = new MinHeap<number>((a, b) => a < b); // the k largest values so far; the smallest is on top

  constructor(k: number, nums: number[]) {
    this.k = k;
    for (const x of nums) this.add(x);
  }

  add(value: number): number {
    this.heap.push(value);
    if (this.heap.size > this.k) this.heap.pop(); // drop the smallest: it is no longer in the top k
    return this.heap.peek()!;
  }
}

const board = new KthLargest(3, [40, 75, 20, 90]);
assert.equal(board.add(50), 50);
assert.equal(board.add(10), 50);
assert.equal(board.add(80), 75);
assert.equal(board.add(95), 80);

const single = new KthLargest(1, []);
assert.equal(single.add(-3), -3);
assert.equal(single.add(-5), -3);
assert.equal(single.add(2), 2);

const ties = new KthLargest(2, [7]);
assert.equal(ties.add(7), 7);
assert.equal(ties.add(3), 7);
assert.equal(ties.add(9), 7);
assert.equal(ties.add(8), 8);

const many = new KthLargest(4, [5, 1, 9, 3, 7, 2]);
assert.equal(many.add(4), 4);
assert.equal(many.add(6), 5);
assert.equal(many.add(10), 6);
console.log("141 ok");
