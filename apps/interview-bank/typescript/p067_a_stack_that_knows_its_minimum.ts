// 067 · A Stack That Knows Its Minimum
import assert from "node:assert/strict";

export class MinStack {
  values: number[] = [];
  mins: number[] = []; // mins[i] is the smallest of values[0..i]

  push(x: number): void {
    this.values.push(x);
    this.mins.push(this.mins.length === 0 ? x : Math.min(x, this.mins[this.mins.length - 1]));
  }

  pop(): number {
    this.mins.pop();
    return this.values.pop()!;
  }

  top(): number {
    return this.values[this.values.length - 1];
  }

  min(): number {
    return this.mins[this.mins.length - 1];
  }
}

const s = new MinStack();
s.push(5);
s.push(3);
s.push(7);
assert.equal(s.min(), 3);
assert.equal(s.top(), 7);
assert.equal(s.pop(), 7);
assert.equal(s.min(), 3);
assert.equal(s.pop(), 3);
assert.equal(s.min(), 5);
assert.equal(s.pop(), 5);

s.push(4);
s.push(1);
s.push(1);
assert.equal(s.pop(), 1);
assert.equal(s.min(), 1);
assert.equal(s.pop(), 1);
assert.equal(s.min(), 4);
s.push(-2);
assert.equal(s.min(), -2);
assert.equal(s.top(), -2);
assert.equal(s.pop(), -2);
assert.equal(s.top(), 4);
assert.equal(s.min(), 4);
console.log("067 ok");
