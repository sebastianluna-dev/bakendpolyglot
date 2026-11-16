// 177 · Edit Distance
import assert from "node:assert/strict";

export function editDistance(source: string, target: string): number {
  const m = target.length;
  // cur[j]: fewest edits turning the first i letters of source into the first j of target; prev holds row i - 1
  let prev = Array.from({ length: m + 1 }, (_, j) => j);
  for (let i = 1; i <= source.length; i++) {
    const cur = new Array<number>(m + 1).fill(0);
    cur[0] = i;
    for (let j = 1; j <= m; j++) {
      if (source[i - 1] === target[j - 1]) cur[j] = prev[j - 1];
      else cur[j] = 1 + Math.min(prev[j], cur[j - 1], prev[j - 1]); // delete, insert, replace
    }
    prev = cur;
  }
  return prev[m];
}

assert.equal(editDistance("shelf", "shelves"), 3);
assert.equal(editDistance("loan", "lone"), 2);
assert.equal(editDistance("", "abc"), 3);
assert.equal(editDistance("abc", ""), 3);
assert.equal(editDistance("seat", "seat"), 0);
assert.equal(editDistance("parcel", "pencil"), 3);
assert.equal(editDistance("trucks", "truck"), 1);
assert.equal(editDistance("abc", "cab"), 2);
assert.equal(editDistance("booking", "looking"), 1);
console.log("177 ok");
