// 048 · Remove Duplicates From a Sorted Array
import assert from "node:assert/strict";

export function removeDuplicates(ids: number[]): number {
  if (ids.length === 0) return 0;
  let write = 1; // ids[0..write) holds the distinct ids kept so far
  for (let read = 1; read < ids.length; read++) {
    if (ids[read] !== ids[write - 1]) ids[write++] = ids[read];
  }
  return write;
}

function deduped(ids: number[]): number[] {
  const k = removeDuplicates(ids);
  return ids.slice(0, k);
}

assert.deepEqual(deduped([3, 3, 5, 8, 8, 8, 9]), [3, 5, 8, 9]);
assert.deepEqual(deduped([7, 7, 7]), [7]);
assert.deepEqual(deduped([]), []);
assert.deepEqual(deduped([1, 2, 3]), [1, 2, 3]);
assert.deepEqual(deduped([10, 10, 20, 20, 30]), [10, 20, 30]);
console.log("048 ok");
