// 178 · Pattern Matching With . and *
import assert from "node:assert/strict";

export function matchesPattern(text: string, pattern: string): boolean {
  const m = pattern.length;
  // cur[j]: the first i letters of text match the first j characters of pattern; prev holds row i - 1
  let prev = new Array<boolean>(m + 1).fill(false);
  for (let i = 0; i <= text.length; i++) {
    const cur = new Array<boolean>(m + 1).fill(false);
    cur[0] = i === 0;
    for (let j = 1; j <= m; j++) {
      if (pattern[j - 1] === "*") {
        // the starred item matches nothing, or it takes text[i - 1] and may take more
        cur[j] = cur[j - 2] || (i > 0 && fits(text[i - 1], pattern[j - 2]) && prev[j]);
      } else {
        cur[j] = i > 0 && fits(text[i - 1], pattern[j - 1]) && prev[j - 1];
      }
    }
    prev = cur;
  }
  return prev[m];
}

function fits(c: string, p: string): boolean {
  return p === "." || p === c;
}

assert.equal(matchesPattern("seat", "se.*t"), true);
assert.equal(matchesPattern("seats", "se.*t"), false);
assert.equal(matchesPattern("", "a*b*"), true);
assert.equal(matchesPattern("", ""), true);
assert.equal(matchesPattern("a", ""), false);
assert.equal(matchesPattern("", "."), false);
assert.equal(matchesPattern("xxy", "z*x*y"), true);
assert.equal(matchesPattern("ac", "a.c"), false);
assert.equal(matchesPattern("aaa", "a*a"), true);
assert.equal(matchesPattern("bbb", "bc*b*d*b"), true);
assert.equal(matchesPattern("b", "a*b"), true);
assert.equal(matchesPattern("ab", ".*"), true);
assert.equal(matchesPattern("abcd", "d*"), false);
assert.equal(matchesPattern("aaaaaaaaaaaaaaaaaaab", "a*a*a*a*a*a*a*a*c"), false);
console.log("178 ok");
