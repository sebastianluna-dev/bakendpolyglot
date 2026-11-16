// 147 · Single-Worker Job Order
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

export function jobOrder(jobs: number[][]): number[] {
  const byArrival = jobs.map((_, i) => i).sort((a, b) => jobs[a][0] - jobs[b][0]);
  // job indices: shortest duration first, then the smaller index
  const ready = new MinHeap<number>(
    (a, b) => jobs[a][1] < jobs[b][1] || (jobs[a][1] === jobs[b][1] && a < b),
  );
  const order: number[] = [];
  let time = 0;
  let next = 0; // position in byArrival of the first job not yet queued
  while (order.length < jobs.length) {
    if (ready.size === 0 && time < jobs[byArrival[next]][0]) time = jobs[byArrival[next]][0]; // idle
    while (next < jobs.length && jobs[byArrival[next]][0] <= time) ready.push(byArrival[next++]);
    const j = ready.pop()!;
    time += jobs[j][1];
    order.push(j);
  }
  return order;
}

assert.deepEqual(jobOrder([[1, 3], [2, 1], [3, 2], [4, 1]]), [0, 1, 3, 2]);
assert.deepEqual(jobOrder([[5, 2], [5, 2], [5, 1], [20, 1]]), [2, 0, 1, 3]);
assert.deepEqual(jobOrder([[7, 4]]), [0]);
assert.deepEqual(jobOrder([[1, 2], [3, 5], [3, 1]]), [0, 2, 1]);
assert.deepEqual(jobOrder([[9, 1], [1, 1]]), [1, 0]);
assert.deepEqual(jobOrder([[1, 1000000000], [1, 1000000000], [1000000000, 1]]), [0, 2, 1]);
console.log("147 ok");
