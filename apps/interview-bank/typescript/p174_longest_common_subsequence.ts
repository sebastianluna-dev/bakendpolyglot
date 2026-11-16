// 174 · Longest Common Subsequence
import assert from "node:assert/strict";

export function longestCommonSubsequence(a: string, b: string): number {
  const m = b.length;
  // cur[j]: answer for the first i letters of a and the first j of b; prev holds row i - 1
  let prev = new Array<number>(m + 1).fill(0);
  for (let i = 1; i <= a.length; i++) {
    const cur = new Array<number>(m + 1).fill(0);
    for (let j = 1; j <= m; j++) {
      if (a[i - 1] === b[j - 1]) cur[j] = prev[j - 1] + 1;
      else cur[j] = Math.max(prev[j], cur[j - 1]);
    }
    prev = cur;
  }
  return prev[m];
}

assert.equal(longestCommonSubsequence("parcel", "place"), 4);
assert.equal(longestCommonSubsequence("seat", "lock"), 0);
assert.equal(longestCommonSubsequence("", "abc"), 0);
assert.equal(longestCommonSubsequence("abc", ""), 0);
assert.equal(longestCommonSubsequence("member", "member"), 6);
assert.equal(longestCommonSubsequence("aaaa", "aa"), 2);
assert.equal(longestCommonSubsequence("delivery", "relived"), 5);
console.log("174 ok");
