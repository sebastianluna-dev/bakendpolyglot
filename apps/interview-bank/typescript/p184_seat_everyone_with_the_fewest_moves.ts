// 184 · Seat Everyone With the Fewest Moves
import assert from "node:assert/strict";

export function fewestMoves(seats: number[], guests: number[]): number {
  const s = seats.toSorted((a, b) => a - b);
  const g = guests.toSorted((a, b) => a - b);
  let moves = 0;
  for (let i = 0; i < s.length; i++) moves += Math.abs(s[i] - g[i]);
  return moves;
}

assert.equal(fewestMoves([4, 1, 7], [2, 8, 3]), 3);
assert.equal(fewestMoves([2, 6, 9, 10], [5, 5, 1, 12]), 8);
assert.equal(fewestMoves([], []), 0);
assert.equal(fewestMoves([3, 5], [5, 3]), 0);
assert.equal(fewestMoves([10], [1]), 9);
console.log("184 ok");
