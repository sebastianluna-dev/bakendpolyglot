// 130 · Service Startup Order
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

export function startupOrder(n: number, dependencies: number[][]): number[] {
  const dependents: number[][] = Array.from({ length: n }, () => []);
  const waiting = new Array<number>(n).fill(0); // dependencies not yet started
  for (const [service, needs] of dependencies) {
    dependents[needs].push(service);
    waiting[service]++;
  }
  const ready = new MinHeap<number>((a, b) => a < b);
  for (let s = 0; s < n; s++) if (waiting[s] === 0) ready.push(s);
  const order: number[] = [];
  while (ready.size > 0) {
    const service = ready.pop()!;
    order.push(service);
    for (const d of dependents[service]) {
      waiting[d]--;
      if (waiting[d] === 0) ready.push(d);
    }
  }
  return order.length === n ? order : [];
}

assert.deepEqual(startupOrder(5, [[0, 3], [2, 4], [1, 0]]), [3, 0, 1, 4, 2]);
assert.deepEqual(startupOrder(3, [[0, 1], [1, 2], [2, 0]]), []);
assert.deepEqual(startupOrder(3, []), [0, 1, 2]);
assert.deepEqual(startupOrder(1, []), [0]);
assert.deepEqual(startupOrder(2, [[1, 1]]), []);
assert.deepEqual(startupOrder(3, [[2, 0], [2, 0], [1, 2]]), [0, 2, 1]);
assert.deepEqual(startupOrder(4, [[1, 0], [2, 3], [3, 2]]), []);
console.log("130 ok");
