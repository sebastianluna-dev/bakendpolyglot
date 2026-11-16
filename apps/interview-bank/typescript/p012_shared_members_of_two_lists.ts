// 012 · Shared Members of Two Lists
import assert from "node:assert/strict";

export function sharedMembers(first: number[], second: number[]): number[] {
  const pending = new Set(second);
  const shared: number[] = [];
  for (const id of first) {
    if (pending.delete(id)) shared.push(id); // removed, so a repeat in first is skipped
  }
  return shared;
}

assert.deepEqual(sharedMembers([104, 211, 104, 350, 87], [87, 104, 999, 104]), [104, 87]);
assert.deepEqual(sharedMembers([1, 2], [3, 4]), []);
assert.deepEqual(sharedMembers([], [1]), []);
assert.deepEqual(sharedMembers([5, 5, 5], [5]), [5]);
assert.deepEqual(sharedMembers([3, 1, 2], [2, 3]), [3, 2]);
console.log("012 ok");
