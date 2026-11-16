// 173 · Ways to Decode a Digit String
import assert from "node:assert/strict";

export function countDecodings(digits: string): number {
  let twoBack = 0; // decodings of the first i - 2 digits
  let oneBack = 1; // decodings of the first i - 1 digits; the empty prefix has one
  for (let i = 1; i <= digits.length; i++) {
    let ways = digits[i - 1] !== "0" ? oneBack : 0;
    if (i >= 2) {
      const two = Number(digits.slice(i - 2, i));
      if (two >= 10 && two <= 26) ways += twoBack; // at least 10: "06" is not a letter
    }
    twoBack = oneBack;
    oneBack = ways;
  }
  return oneBack;
}

assert.equal(countDecodings("2612"), 4);
assert.equal(countDecodings("301"), 0);
assert.equal(countDecodings("1201"), 1);
assert.equal(countDecodings("106"), 1);
assert.equal(countDecodings("0"), 0);
assert.equal(countDecodings("100"), 0);
assert.equal(countDecodings("27"), 1);
assert.equal(countDecodings("11106"), 2);
assert.equal(countDecodings("1".repeat(45)), 1836311903);
console.log("173 ok");
