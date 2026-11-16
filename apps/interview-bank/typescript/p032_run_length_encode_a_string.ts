// 032 · Run-Length Encode a String
import assert from "node:assert/strict";

export function runLengthEncode(s: string): string {
  const parts: string[] = [];
  let i = 0;
  while (i < s.length) {
    let j = i;
    while (j < s.length && s[j] === s[i]) j++;
    parts.push(s[i] + String(j - i));
    i = j;
  }
  return parts.join("");
}

assert.equal(runLengthEncode("aaabccdddd"), "a3b1c2d4");
assert.equal(runLengthEncode("abc"), "a1b1c1");
assert.equal(runLengthEncode(""), "");
assert.equal(runLengthEncode("z".repeat(12)), "z12");
assert.equal(runLengthEncode("aAaa"), "a1A1a2");
assert.equal(runLengthEncode("abb"), "a1b2");
console.log("032 ok");
