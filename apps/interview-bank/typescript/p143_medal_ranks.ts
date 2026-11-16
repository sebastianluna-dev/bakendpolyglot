// 143 · Medal Ranks
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

export function medalRanks(scores: number[]): string[] {
  const heap = new MinHeap<number>((a, b) => scores[a] > scores[b]); // indices, highest score on top
  for (let i = 0; i < scores.length; i++) heap.push(i);
  const medals = ["Gold Medal", "Silver Medal", "Bronze Medal"];
  const ranks = new Array<string>(scores.length);
  for (let place = 1; heap.size > 0; place++) {
    const i = heap.pop()!;
    ranks[i] = place <= 3 ? medals[place - 1] : String(place);
  }
  return ranks;
}

assert.deepEqual(medalRanks([88, 95, 70, 91]), ["Bronze Medal", "Gold Medal", "4", "Silver Medal"]);
assert.deepEqual(medalRanks([3, 9, 1, 12, 5, 7]), ["5", "Silver Medal", "6", "Gold Medal", "4", "Bronze Medal"]);
assert.deepEqual(medalRanks([42]), ["Gold Medal"]);
assert.deepEqual(medalRanks([0, 5]), ["Silver Medal", "Gold Medal"]);
assert.deepEqual(medalRanks([]), []);
assert.deepEqual(
  medalRanks([10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110]),
  ["11", "10", "9", "8", "7", "6", "5", "4", "Bronze Medal", "Silver Medal", "Gold Medal"],
);
console.log("143 ok");
