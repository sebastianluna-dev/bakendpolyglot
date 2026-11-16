// 134 · Time for a Signal to Reach Every Node
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

export function signalTime(n: number, links: number[][], source: number): number {
  const graph: [number, number][][] = Array.from({ length: n }, () => []);
  for (const [from, to, ms] of links) graph[from].push([to, ms]);
  const dist = new Array<number>(n).fill(Infinity);
  dist[source] = 0;
  const heap = new MinHeap<[number, number]>((a, b) => a[0] < b[0]); // [distance, node]
  heap.push([0, source]);
  while (heap.size > 0) {
    const [d, node] = heap.pop()!;
    if (d > dist[node]) continue; // stale: a shorter path was found after this was queued
    for (const [next, ms] of graph[node]) {
      if (d + ms < dist[next]) {
        dist[next] = d + ms;
        heap.push([d + ms, next]);
      }
    }
  }
  const slowest = Math.max(...dist);
  return slowest === Infinity ? -1 : slowest;
}

assert.equal(signalTime(4, [[0, 1, 4], [0, 2, 1], [2, 1, 2], [1, 3, 1], [2, 3, 6]], 0), 4);
assert.equal(signalTime(3, [[0, 1, 2], [2, 1, 1]], 0), -1);
assert.equal(signalTime(1, [], 0), 0);
assert.equal(signalTime(2, [[0, 1, 5], [0, 1, 2]], 0), 2);
assert.equal(signalTime(2, [[0, 1, 3]], 1), -1);
assert.equal(signalTime(2, [[1, 0, 3]], 1), 3);
assert.equal(signalTime(3, [[0, 1, 1], [1, 2, 1], [2, 0, 1]], 1), 2);
assert.equal(signalTime(3, [[0, 1, 0], [1, 2, 0]], 0), 0);
console.log("134 ok");
