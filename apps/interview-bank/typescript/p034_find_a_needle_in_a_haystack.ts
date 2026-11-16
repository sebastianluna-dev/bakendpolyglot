// 034 · Find a Needle in a Haystack
import assert from "node:assert/strict";

export function findNeedle(haystack: string, needle: string): number {
  for (let start = 0; start + needle.length <= haystack.length; start++) {
    let k = 0;
    while (k < needle.length && haystack[start + k] === needle[k]) k++;
    if (k === needle.length) return start;
  }
  return -1;
}

assert.equal(findNeedle("hold-14-seat-14", "14"), 5);
assert.equal(findNeedle("parcel", "pulse"), -1);
assert.equal(findNeedle("abc", "c"), 2);
assert.equal(findNeedle("aaab", "ab"), 2);
assert.equal(findNeedle("abc", ""), 0);
assert.equal(findNeedle("", ""), 0);
assert.equal(findNeedle("", "a"), -1);
assert.equal(findNeedle("ab", "abc"), -1);
console.log("034 ok");
