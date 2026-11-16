// 046 · Is One a Subsequence of the Other?
import assert from "node:assert/strict";

export function isSubsequence(plan: string, scans: string): boolean {
  let i = 0;
  let j = 0;
  while (i < plan.length && j < scans.length) {
    if (plan[i] === scans[j]) i++;
    j++;
  }
  return i === plan.length;
}

assert.equal(isSubsequence("ACE", "ABCDE"), true);
assert.equal(isSubsequence("AEC", "ABCDE"), false);
assert.equal(isSubsequence("", "ABC"), true);
assert.equal(isSubsequence("A", ""), false);
assert.equal(isSubsequence("AA", "ABA"), true);
assert.equal(isSubsequence("AAB", "ABAC"), false);
console.log("046 ok");
