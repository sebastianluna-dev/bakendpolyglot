// 057 · Contains a Scrambled Copy
import assert from "node:assert/strict";

export function containsScrambled(pattern: string, text: string): boolean {
  const m = pattern.length;
  if (m > text.length) return false;
  const need = new Array<number>(26).fill(0);
  const have = new Array<number>(26).fill(0);
  for (let i = 0; i < m; i++) {
    need[pattern.charCodeAt(i) - 97]++;
    have[text.charCodeAt(i) - 97]++;
  }
  const same = () => need.every((n, c) => n === have[c]);
  if (same()) return true;
  for (let i = m; i < text.length; i++) {
    have[text.charCodeAt(i) - 97]++; // letter entering the window
    have[text.charCodeAt(i - m) - 97]--; // letter leaving it
    if (same()) return true;
  }
  return false;
}

assert.equal(containsScrambled("loan", "renewalnoted"), true);
assert.equal(containsScrambled("seat", "stage"), false);
assert.equal(containsScrambled("abc", "ab"), false);
assert.equal(containsScrambled("ab", "ab"), true);
assert.equal(containsScrambled("aab", "baxaba"), true);
assert.equal(containsScrambled("aab", "abbb"), false);
assert.equal(containsScrambled("z", "abcz"), true);
console.log("057 ok");
