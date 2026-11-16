// 149 · Running Median
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

export class RunningMedian {
  low = new MinHeap<number>((a, b) => a > b); // the smaller half, largest on top
  high = new MinHeap<number>((a, b) => a < b); // the larger half, smallest on top

  addNum(num: number): void {
    this.low.push(num);
    this.high.push(this.low.pop()!);
    if (this.high.size > this.low.size) this.low.push(this.high.pop()!);
  }

  findMedian(): number {
    if (this.low.size > this.high.size) return this.low.peek()!;
    return (this.low.peek()! + this.high.peek()!) / 2;
  }
}

const m = new RunningMedian();
m.addNum(5);
assert.equal(m.findMedian(), 5);
m.addNum(15);
assert.equal(m.findMedian(), 10);
m.addNum(1);
assert.equal(m.findMedian(), 5);
m.addNum(3);
assert.equal(m.findMedian(), 4);

const neg = new RunningMedian();
neg.addNum(-2);
assert.equal(neg.findMedian(), -2);
neg.addNum(-2);
assert.equal(neg.findMedian(), -2);
neg.addNum(7);
assert.equal(neg.findMedian(), -2);
neg.addNum(8);
assert.equal(neg.findMedian(), 2.5);

const down = new RunningMedian();
const medians: number[] = [];
for (const x of [9, 8, 7, 6, 5]) {
  down.addNum(x);
  medians.push(down.findMedian());
}
assert.deepEqual(medians, [9, 8.5, 8, 7.5, 7]);

const big = new RunningMedian();
big.addNum(2_147_483_647);
big.addNum(2_147_483_647);
assert.equal(big.findMedian(), 2_147_483_647);
big.addNum(-2_147_483_648);
big.addNum(-2_147_483_648);
assert.equal(big.findMedian(), -0.5);
console.log("149 ok");
