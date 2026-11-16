// 045 · Shortest Palindrome by Prepending
import assert from "node:assert/strict";

export function shortestPalindrome(s: string): string {
  const reversed = [...s].reverse().join("");
  const t = s + "#" + reversed; // the '#' stops a match from running past the end of s
  const fail = new Array<number>(t.length).fill(0);
  let k = 0;
  for (let i = 1; i < t.length; i++) {
    while (k > 0 && t[i] !== t[k]) k = fail[k - 1];
    if (t[i] === t[k]) k++;
    fail[i] = k;
  }
  const keep = fail[t.length - 1]; // length of the longest palindromic prefix of s
  return reversed.slice(0, s.length - keep) + s;
}

assert.equal(shortestPalindrome("abacd"), "dcabacd");
assert.equal(shortestPalindrome("stats"), "stats");
assert.equal(shortestPalindrome(""), "");
assert.equal(shortestPalindrome("a"), "a");
assert.equal(shortestPalindrome("ab"), "bab");
assert.equal(shortestPalindrome("aabba"), "abbaabba");
assert.equal(shortestPalindrome("aaa"), "aaa");
console.log("045 ok");
