// 006 · The Unpaired Seat
import assert from "node:assert/strict";

export function unpairedSeat(log: number[]): number {
  let result = 0;
  for (const seat of log) result ^= seat; // pairs cancel: x ^ x is 0
  return result;
}

assert.equal(unpairedSeat([14, 3, 14, 8, 3]), 8);
assert.equal(unpairedSeat([21]), 21);
assert.equal(unpairedSeat([4, 4, 9]), 9);
assert.equal(unpairedSeat([7, 2, 2]), 7);
assert.equal(unpairedSeat([1000000000, 5, 5]), 1000000000);
console.log("006 ok");
