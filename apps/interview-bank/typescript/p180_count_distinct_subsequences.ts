// 180 · Count Distinct Subsequences
import assert from "node:assert/strict";

const MOD = 1_000_000_007;

export function countSubsequences(s: string, t: string): number {
  const m = t.length;
  const ways = new Array<number>(m + 1).fill(0); // ways[j]: ways to spell the first j letters of t so far
  ways[0] = 1;
  for (const c of s) {
    for (let j = m; j >= 1; j--) { // downwards, so this letter of s is used at most once
      if (t[j - 1] === c) ways[j] = (ways[j] + ways[j - 1]) % MOD;
    }
  }
  return ways[m];
}

assert.equal(countSubsequences("banana", "ban"), 3);
assert.equal(countSubsequences("seatseat", "sat"), 4);
assert.equal(countSubsequences("abc", "abcd"), 0);
assert.equal(countSubsequences("abc", ""), 1);
assert.equal(countSubsequences("", "a"), 0);
assert.equal(countSubsequences("", ""), 1);
assert.equal(countSubsequences("aaaa", "aa"), 6);
assert.equal(countSubsequences("a", "aa"), 0);
assert.equal(countSubsequences("a".repeat(1000), "a".repeat(500)), 159835829);
console.log("180 ok");
