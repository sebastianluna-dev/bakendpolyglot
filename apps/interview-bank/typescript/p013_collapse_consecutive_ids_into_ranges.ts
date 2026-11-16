// 013 · Collapse Consecutive IDs Into Ranges
import assert from "node:assert/strict";

export function collapseRanges(ids: number[]): string[] {
  const ranges: string[] = [];
  let i = 0;
  while (i < ids.length) {
    const start = ids[i];
    while (i + 1 < ids.length && ids[i + 1] === ids[i] + 1) i++;
    ranges.push(start === ids[i] ? `${start}` : `${start}-${ids[i]}`);
    i++;
  }
  return ranges;
}

assert.deepEqual(collapseRanges([1, 2, 3, 7, 9, 10]), ["1-3", "7", "9-10"]);
assert.deepEqual(collapseRanges([0, 2, 4]), ["0", "2", "4"]);
assert.deepEqual(collapseRanges([]), []);
assert.deepEqual(collapseRanges([5]), ["5"]);
assert.deepEqual(collapseRanges([40, 41, 42, 43]), ["40-43"]);
console.log("013 ok");
