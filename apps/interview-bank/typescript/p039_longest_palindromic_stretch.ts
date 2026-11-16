// 039 · Longest Palindromic Stretch
import assert from "node:assert/strict";

export function longestPalindrome(s: string): string {
  let bestStart = 0;
  let bestLength = 0;
  for (let i = 0; i < s.length; i++) {
    for (const length of [expand(s, i, i), expand(s, i, i + 1)]) {
      if (length > bestLength) { // strictly longer, so the first stretch wins a tie
        bestLength = length;
        bestStart = i - Math.floor((length - 1) / 2);
      }
    }
  }
  return s.slice(bestStart, bestStart + bestLength);
}

function expand(s: string, lo: number, hi: number): number {
  while (lo >= 0 && hi < s.length && s[lo] === s[hi]) {
    lo--;
    hi++;
  }
  return hi - lo - 1;
}

assert.equal(longestPalindrome("xabbaracecarz"), "racecar");
assert.equal(longestPalindrome("abcde"), "a");
assert.equal(longestPalindrome("xyzzyabba"), "yzzy");
assert.equal(longestPalindrome("banana"), "anana");
assert.equal(longestPalindrome("abacab"), "bacab");
assert.equal(longestPalindrome("aaaa"), "aaaa");
assert.equal(longestPalindrome("a"), "a");
assert.equal(longestPalindrome(""), "");
console.log("039 ok");
