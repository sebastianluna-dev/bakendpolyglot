// 146 · Rearrange So No Neighbours Match
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

export function rearrange(s: string): string {
  const counts = new Map<string, number>();
  for (const ch of s) counts.set(ch, (counts.get(ch) ?? 0) + 1);
  // [copies left, letter]: most copies first, then the earlier letter
  const heap = new MinHeap<[number, string]>((a, b) => a[0] > b[0] || (a[0] === b[0] && a[1] < b[1]));
  for (const [ch, c] of counts) heap.push([c, ch]);
  const out: string[] = [];
  let held: [number, string] = [0, ""]; // the letter just placed, kept out of the heap for one step
  while (heap.size > 0) {
    const [c, ch] = heap.pop()!;
    out.push(ch);
    if (held[0] > 0) heap.push(held);
    held = [c - 1, ch];
  }
  return out.length === s.length ? out.join("") : "";
}

assert.equal(rearrange("aab"), "aba");
assert.equal(rearrange("aaab"), "");
assert.equal(rearrange("vvvlo"), "vlvov");
assert.equal(rearrange("aabbcc"), "abcabc");
assert.equal(rearrange("aaabbc"), "ababac");
assert.equal(rearrange("a"), "a");
assert.equal(rearrange("zz"), "");
console.log("146 ok");
