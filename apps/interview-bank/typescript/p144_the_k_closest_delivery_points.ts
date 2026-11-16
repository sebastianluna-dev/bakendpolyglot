// 144 · The k Closest Delivery Points
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

function compare(a: number[], b: number[]): number {
  return a[0] * a[0] + a[1] * a[1] - (b[0] * b[0] + b[1] * b[1]) || a[0] - b[0] || a[1] - b[1];
}

export function kClosest(points: number[][], k: number): number[][] {
  const heap = new MinHeap<number[]>((a, b) => compare(a, b) > 0); // farthest kept point on top
  for (const p of points) {
    heap.push(p);
    if (heap.size > k) heap.pop();
  }
  const result = new Array<number[]>(heap.size);
  for (let i = result.length - 1; i >= 0; i--) result[i] = heap.pop()!;
  return result;
}

assert.deepEqual(kClosest([[3, 4], [1, -2], [-2, 1], [0, 5], [2, 2]], 3), [[-2, 1], [1, -2], [2, 2]]);
assert.deepEqual(kClosest([[2, 0], [0, 2], [0, -2], [-2, 0]], 2), [[-2, 0], [0, -2]]);
assert.deepEqual(kClosest([[5, 5], [1, 1]], 0), []);
assert.deepEqual(kClosest([[6, 1], [-1, -1], [0, 0]], 3), [[0, 0], [-1, -1], [6, 1]]);
assert.deepEqual(kClosest([[1, 1], [0, 3], [1, 1]], 2), [[1, 1], [1, 1]]);
assert.deepEqual(kClosest([[10000, -10000], [-10000, 10000]], 1), [[-10000, 10000]]);
console.log("144 ok");
