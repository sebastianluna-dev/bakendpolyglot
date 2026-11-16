// 070 · Remove Adjacent Pairs
import assert from "node:assert/strict";

export function removePairs(s: string): string {
  const kept: string[] = []; // used as a stack: the result so far
  for (const ch of s) {
    if (kept.length > 0 && kept[kept.length - 1] === ch) kept.pop();
    else kept.push(ch);
  }
  return kept.join("");
}

assert.equal(removePairs("abccbd"), "ad");
assert.equal(removePairs("aabb"), "");
assert.equal(removePairs(""), "");
assert.equal(removePairs("abc"), "abc");
assert.equal(removePairs("aaa"), "a");
assert.equal(removePairs("abba"), "");
assert.equal(removePairs("mississippi"), "m");
console.log("070 ok");
