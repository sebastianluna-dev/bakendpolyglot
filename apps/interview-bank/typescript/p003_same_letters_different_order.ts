// 003 · Same Letters, Different Order
import assert from "node:assert/strict";

export function sameLetters(a: string, b: string): boolean {
  if (a.length !== b.length) return false;
  const counts = new Array<number>(26).fill(0);
  for (let i = 0; i < a.length; i++) {
    counts[a.charCodeAt(i) - 97]++;
    counts[b.charCodeAt(i) - 97]--;
  }
  return counts.every((c) => c === 0);
}

assert.equal(sameLetters("dusty", "study"), true);
assert.equal(sameLetters("aab", "abb"), false);
assert.equal(sameLetters("loan", "loans"), false);
assert.equal(sameLetters("abc", "abc"), true);
assert.equal(sameLetters("", ""), true);
console.log("003 ok");
