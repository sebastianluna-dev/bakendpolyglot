// 056 · Longest Run After k Replacements
import assert from "node:assert/strict";

export function longestRunAfterReplacements(s: string, k: number): number {
  const counts = new Array<number>(26).fill(0);
  let left = 0;
  let best = 0;
  for (let right = 0; right < s.length; right++) {
    counts[s.charCodeAt(right) - 65]++;
    // Letters to replace = window length minus the most common letter in it.
    while (right - left + 1 - Math.max(...counts) > k) {
      counts[s.charCodeAt(left) - 65]--;
      left++;
    }
    best = Math.max(best, right - left + 1);
  }
  return best;
}

assert.equal(longestRunAfterReplacements("ABAACBA", 1), 4);
assert.equal(longestRunAfterReplacements("XYXY", 2), 4);
assert.equal(longestRunAfterReplacements("", 0), 0);
assert.equal(longestRunAfterReplacements("AAAA", 0), 4);
assert.equal(longestRunAfterReplacements("ABCD", 0), 1);
assert.equal(longestRunAfterReplacements("AB", 5), 2);
assert.equal(longestRunAfterReplacements("BAAABBBBA", 2), 6);
console.log("056 ok");
