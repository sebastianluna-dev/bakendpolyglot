// 049 · Push the Zeros to the End
import assert from "node:assert/strict";

export function pushZerosToEnd(slots: number[]): void {
  let write = 0; // slots[0..write) holds the parcels seen so far, in order
  for (let read = 0; read < slots.length; read++) {
    if (slots[read] !== 0) {
      [slots[write], slots[read]] = [slots[read], slots[write]];
      write++;
    }
  }
}

function pushed(slots: number[]): number[] {
  pushZerosToEnd(slots);
  return slots;
}

assert.deepEqual(pushed([0, 41, 0, 0, 17, 23]), [41, 17, 23, 0, 0, 0]);
assert.deepEqual(pushed([5, 0, 3]), [5, 3, 0]);
assert.deepEqual(pushed([]), []);
assert.deepEqual(pushed([0, 0]), [0, 0]);
assert.deepEqual(pushed([1, 2]), [1, 2]);
assert.deepEqual(pushed([0, 0, 1]), [1, 0, 0]);
console.log("049 ok");
