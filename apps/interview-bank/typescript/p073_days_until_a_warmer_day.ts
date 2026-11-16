// 073 · Days Until a Warmer Day
import assert from "node:assert/strict";

export function daysUntilWarmer(temps: number[]): number[] {
  const wait = new Array<number>(temps.length).fill(0);
  const stack: number[] = []; // indices still waiting, their temperatures never rising from bottom to top
  for (let i = 0; i < temps.length; i++) {
    while (stack.length > 0 && temps[stack[stack.length - 1]] < temps[i]) {
      const j = stack.pop()!;
      wait[j] = i - j;
    }
    stack.push(i);
  }
  return wait;
}

assert.deepEqual(daysUntilWarmer([18, 16, 17, 21, 20, 20, 23]), [3, 1, 1, 3, 2, 1, 0]);
assert.deepEqual(daysUntilWarmer([30, 25, 20]), [0, 0, 0]);
assert.deepEqual(daysUntilWarmer([]), []);
assert.deepEqual(daysUntilWarmer([12]), [0]);
assert.deepEqual(daysUntilWarmer([5, 5, 5]), [0, 0, 0]);
assert.deepEqual(daysUntilWarmer([-3, -1, 0, 2]), [1, 1, 1, 0]);
console.log("073 ok");
