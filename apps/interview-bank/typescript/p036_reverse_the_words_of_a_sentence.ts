// 036 · Reverse the Words of a Sentence
import assert from "node:assert/strict";

export function reverseWords(sentence: string): string {
  const words = sentence.split(" ").filter((w) => w !== "");
  return words.reverse().join(" ");
}

assert.equal(reverseWords("seat 14 is free"), "free is 14 seat");
assert.equal(reverseWords("  return   the copy "), "copy the return");
assert.equal(reverseWords("hold"), "hold");
assert.equal(reverseWords(""), "");
assert.equal(reverseWords("   "), "");
assert.equal(reverseWords("a b c"), "c b a");
console.log("036 ok");
