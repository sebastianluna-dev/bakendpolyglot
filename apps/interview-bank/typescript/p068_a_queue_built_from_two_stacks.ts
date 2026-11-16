// 068 · A Queue Built From Two Stacks
import assert from "node:assert/strict";

export class TwoStackQueue {
  inbox: number[] = []; // newest on top
  outbox: number[] = []; // oldest on top

  enqueue(x: number): void {
    this.inbox.push(x);
  }

  dequeue(): number {
    this.refill();
    return this.outbox.pop()!;
  }

  peek(): number {
    this.refill();
    return this.outbox[this.outbox.length - 1];
  }

  isEmpty(): boolean {
    return this.inbox.length === 0 && this.outbox.length === 0;
  }

  // Only when outbox is empty, or newer items would jump ahead of older ones.
  refill(): void {
    if (this.outbox.length > 0) return;
    while (this.inbox.length > 0) this.outbox.push(this.inbox.pop()!);
  }
}

const q = new TwoStackQueue();
assert.equal(q.isEmpty(), true);
q.enqueue(1);
q.enqueue(2);
assert.equal(q.peek(), 1);
assert.equal(q.dequeue(), 1);
q.enqueue(3);
assert.equal(q.dequeue(), 2);
assert.equal(q.peek(), 3);
assert.equal(q.isEmpty(), false);
assert.equal(q.dequeue(), 3);
assert.equal(q.isEmpty(), true);
q.enqueue(4);
q.enqueue(5);
assert.equal(q.dequeue(), 4);
q.enqueue(6);
assert.equal(q.dequeue(), 5);
assert.equal(q.dequeue(), 6);
assert.equal(q.isEmpty(), true);
console.log("068 ok");
