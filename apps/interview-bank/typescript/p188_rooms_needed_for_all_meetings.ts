// 188 · Rooms Needed for All Meetings
import assert from "node:assert/strict";

export function roomsNeeded(meetings: number[][]): number {
  const starts = meetings.map((m) => m[0]).sort((a, b) => a - b);
  const ends = meetings.map((m) => m[1]).sort((a, b) => a - b);
  let rooms = 0;
  let j = 0; // ends[j] is the earliest end that has not freed a room yet
  for (const start of starts) {
    if (start >= ends[j]) {
      j++; // a meeting is over: reuse its room
    } else {
      rooms++;
    }
  }
  return rooms;
}

assert.equal(roomsNeeded([[9, 12], [10, 11], [11, 13], [13, 14]]), 2);
assert.equal(roomsNeeded([[8, 10], [10, 12], [12, 14]]), 1);
assert.equal(roomsNeeded([]), 0);
assert.equal(roomsNeeded([[1, 5], [2, 6], [3, 7]]), 3);
assert.equal(roomsNeeded([[2, 3], [2, 3]]), 2);
assert.equal(roomsNeeded([[5, 10], [1, 4], [4, 6]]), 2);
console.log("188 ok");
