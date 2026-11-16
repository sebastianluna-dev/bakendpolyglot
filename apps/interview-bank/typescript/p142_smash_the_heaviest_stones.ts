// 142 · Smash the Heaviest Stones
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

export function lastStone(stones: number[]): number {
  const heap = new MinHeap<number>((a, b) => a > b); // a max-heap: the heaviest stone on top
  for (const s of stones) heap.push(s);
  while (heap.size > 1) {
    const x = heap.pop()!;
    const y = heap.pop()!;
    if (x > y) heap.push(x - y);
  }
  return heap.peek() ?? 0;
}

assert.equal(lastStone([3, 9, 4, 4, 1]), 1);
assert.equal(lastStone([10, 4, 4, 2]), 0);
assert.equal(lastStone([6, 6]), 0);
assert.equal(lastStone([7]), 7);
assert.equal(lastStone([1, 1, 1]), 1);
assert.equal(lastStone([2, 2, 3]), 1);
assert.equal(lastStone([5, 1, 1, 1, 1, 1]), 0);
console.log("142 ok");
