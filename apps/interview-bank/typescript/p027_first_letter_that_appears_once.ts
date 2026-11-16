// 027 · First Letter That Appears Once
import assert from "node:assert/strict";

export function firstUniqueIndex(word: string): number {
  const counts = new Array<number>(26).fill(0);
  for (const ch of word) counts[ch.charCodeAt(0) - 97]++;
  for (let i = 0; i < word.length; i++) {
    if (counts[word.charCodeAt(i) - 97] === 1) return i;
  }
  return -1;
}

assert.equal(firstUniqueIndex("shelfwise"), 1);
assert.equal(firstUniqueIndex("noon"), -1);
assert.equal(firstUniqueIndex(""), -1);
assert.equal(firstUniqueIndex("z"), 0);
assert.equal(firstUniqueIndex("aabbc"), 4);
assert.equal(firstUniqueIndex("pear"), 0);
console.log("027 ok");
