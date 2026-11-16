// 065 · Maximum of Every Window
import assert from "node:assert/strict";

export function windowMaxima(readings: number[], k: number): number[] {
  const result: number[] = [];
  const window: number[] = []; // indices; their readings decrease from window[head] to the back
  let head = 0;
  for (let i = 0; i < readings.length; i++) {
    while (window.length > head && readings[window[window.length - 1]] <= readings[i]) window.pop();
    window.push(i);
    if (window[head] <= i - k) head++;
    if (i >= k - 1) result.push(readings[window[head]]);
  }
  return result;
}

assert.deepEqual(windowMaxima([3, 1, -2, 5, 4, 2, 6], 3), [3, 5, 5, 5, 6]);
assert.deepEqual(windowMaxima([2, 7, 1], 3), [7]);
assert.deepEqual(windowMaxima([4, -1, 4], 1), [4, -1, 4]);
assert.deepEqual(windowMaxima([], 2), []);
assert.deepEqual(windowMaxima([1, 2], 3), []);
assert.deepEqual(windowMaxima([5, 5, 1, 5], 2), [5, 5, 5]);
assert.deepEqual(windowMaxima([9, 7, 5, 3], 2), [9, 7, 5]);
assert.deepEqual(windowMaxima([9, 1, 1, 1], 2), [9, 1, 1]);
console.log("065 ok");
