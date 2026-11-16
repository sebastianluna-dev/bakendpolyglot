// 148 · How Far With Bricks and Ladders
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

export function furthestPlatform(heights: number[], bricks: number, ladders: number): number {
  const onLadders = new MinHeap<number>((a, b) => a < b); // the climbs ladders cover so far
  for (let i = 0; i < heights.length - 1; i++) {
    const climb = heights[i + 1] - heights[i];
    if (climb <= 0) continue;
    onLadders.push(climb);
    if (onLadders.size > ladders) bricks -= onLadders.pop()!; // the smallest climb goes to bricks
    if (bricks < 0) return i;
  }
  return heights.length - 1;
}

assert.equal(furthestPlatform([3, 8, 6, 10, 11, 15, 7], 4, 1), 3);
assert.equal(furthestPlatform([10, 12, 13, 25, 26], 4, 1), 4);
assert.equal(furthestPlatform([7], 0, 0), 0);
assert.equal(furthestPlatform([9, 7, 7, 3], 0, 0), 3);
assert.equal(furthestPlatform([1, 2], 0, 0), 0);
assert.equal(furthestPlatform([1, 2, 3, 4], 0, 3), 3);
assert.equal(furthestPlatform([1, 6, 2, 9, 4, 5], 13, 0), 5);
assert.equal(furthestPlatform([1, 6, 2, 9, 4, 5], 12, 0), 4);
console.log("148 ok");
