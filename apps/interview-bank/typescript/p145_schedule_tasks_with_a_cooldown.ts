// 145 · Schedule Tasks With a Cooldown
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

export function scheduleTime(tasks: string, cooldown: number): number {
  const counts = new Map<string, number>();
  for (const t of tasks) counts.set(t, (counts.get(t) ?? 0) + 1);
  const heap = new MinHeap<number>((a, b) => a > b); // copies left per type, most first
  for (const c of counts.values()) heap.push(c);
  const waiting: [number, number][] = []; // [copies left, time it may run again], oldest first
  let time = 0;
  while (heap.size > 0 || waiting.length > 0) {
    time++;
    if (heap.size === 0) {
      time = waiting[0][1]; // idle until the next type comes off cooldown
    } else {
      const left = heap.pop()! - 1;
      if (left > 0) waiting.push([left, time + cooldown]);
    }
    if (waiting.length > 0 && waiting[0][1] === time) heap.push(waiting.shift()![0]);
  }
  return time;
}

assert.equal(scheduleTime("AAABBC", 2), 7);
assert.equal(scheduleTime("AABBCD", 1), 6);
assert.equal(scheduleTime("AAABBB", 2), 8);
assert.equal(scheduleTime("AAAA", 3), 13);
assert.equal(scheduleTime("ABAB", 0), 4);
assert.equal(scheduleTime("A", 5), 1);
assert.equal(scheduleTime("AAABCDEFG", 2), 9);
console.log("145 ok");
