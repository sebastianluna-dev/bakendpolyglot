// 181 · Can One Person Attend Every Meeting?
import assert from "node:assert/strict";

export function canAttendAll(meetings: number[][]): boolean {
  meetings.sort((a, b) => a[0] - b[0]);
  for (let i = 1; i < meetings.length; i++) {
    if (meetings[i][0] < meetings[i - 1][1]) return false; // starting just as the last one ends is fine
  }
  return true;
}

assert.equal(canAttendAll([[540, 600], [600, 660], [480, 540]]), true);
assert.equal(canAttendAll([[540, 630], [600, 660]]), false);
assert.equal(canAttendAll([]), true);
assert.equal(canAttendAll([[100, 200]]), true);
assert.equal(canAttendAll([[700, 800], [100, 200], [150, 160]]), false);
assert.equal(canAttendAll([[5, 10], [5, 6]]), false);
assert.equal(canAttendAll([[0, 1], [2, 3], [1, 2]]), true);
console.log("181 ok");
