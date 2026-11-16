// 023 · A Set With Random Pick in O(1)
import assert from "node:assert/strict";

export class RandomSet {
  items: number[] = [];
  position = new Map<number, number>(); // value -> its index in items
  randomIndex: (n: number) => number;

  constructor(randomIndex: (n: number) => number) {
    this.randomIndex = randomIndex;
  }

  add(value: number): boolean {
    if (this.position.has(value)) return false;
    this.position.set(value, this.items.length);
    this.items.push(value);
    return true;
  }

  remove(value: number): boolean {
    const i = this.position.get(value);
    if (i === undefined) return false;
    const last = this.items.pop()!;
    if (last !== value) {
      this.items[i] = last;
      this.position.set(last, i);
    }
    this.position.delete(value);
    return true;
  }

  pick(): number {
    return this.items[this.randomIndex(this.items.length)];
  }
}

function picks(set: RandomSet, count: number): number[] {
  const out: number[] = [];
  for (let k = 0; k < count; k++) out.push(set.pick());
  return out.sort((a, b) => a - b);
}

let calls = 0;
const set = new RandomSet((n) => calls++ % n);
assert.equal(set.add(12), true);
assert.equal(set.add(7), true);
assert.equal(set.add(12), false);
assert.equal(set.add(30), true);
assert.equal(set.remove(7), true);
assert.equal(set.remove(7), false);
assert.equal(set.remove(99), false);
assert.deepEqual(picks(set, 2), [12, 30]);
assert.equal(set.add(5), true);
assert.deepEqual(picks(set, 3), [5, 12, 30]);
assert.equal(set.remove(30), true);
assert.equal(set.remove(5), true);
assert.equal(set.add(5), true);
assert.equal(set.remove(12), true);
assert.equal(set.pick(), 5);
console.log("023 ok");
