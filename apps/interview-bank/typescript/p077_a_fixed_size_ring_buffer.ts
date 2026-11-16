// 077 · A Fixed-Size Ring Buffer
import assert from "node:assert/strict";

export class RingBuffer {
  items: number[];
  head = 0; // index of the oldest value
  count = 0;

  constructor(capacity: number) {
    this.items = new Array<number>(capacity).fill(0);
  }

  enqueue(value: number): boolean {
    if (this.isFull()) return false;
    this.items[(this.head + this.count) % this.items.length] = value;
    this.count++;
    return true;
  }

  dequeue(): number {
    if (this.isEmpty()) return -1;
    const value = this.items[this.head];
    this.head = (this.head + 1) % this.items.length;
    this.count--;
    return value;
  }

  front(): number {
    return this.isEmpty() ? -1 : this.items[this.head];
  }

  rear(): number {
    return this.isEmpty() ? -1 : this.items[(this.head + this.count - 1) % this.items.length];
  }

  isEmpty(): boolean {
    return this.count === 0;
  }

  isFull(): boolean {
    return this.count === this.items.length;
  }
}

const b = new RingBuffer(3);
assert.equal(b.isEmpty(), true);
assert.equal(b.enqueue(5), true);
assert.equal(b.enqueue(8), true);
assert.equal(b.enqueue(2), true);
assert.equal(b.enqueue(9), false);
assert.equal(b.isFull(), true);
assert.equal(b.rear(), 2);
assert.equal(b.dequeue(), 5);
assert.equal(b.enqueue(9), true);
assert.equal(b.front(), 8);
assert.equal(b.rear(), 9);
assert.equal(b.dequeue(), 8);
assert.equal(b.dequeue(), 2);
assert.equal(b.dequeue(), 9);
assert.equal(b.dequeue(), -1);
assert.equal(b.isEmpty(), true);
assert.equal(b.front(), -1);
assert.equal(b.rear(), -1);

const one = new RingBuffer(1);
assert.equal(one.enqueue(0), true);
assert.equal(one.isFull(), true);
assert.equal(one.enqueue(1), false);
assert.equal(one.front(), 0);
assert.equal(one.rear(), 0);
assert.equal(one.dequeue(), 0);
assert.equal(one.isEmpty(), true);
console.log("077 ok");
