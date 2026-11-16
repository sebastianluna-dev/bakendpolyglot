// 074 · Expand Repeated Patterns
import assert from "node:assert/strict";

export function expandPatterns(s: string): string {
  const counts: number[] = [];
  const outer: string[] = []; // the text built before each open bracket
  let current = "";
  let count = 0;
  for (const ch of s) {
    if (ch >= "0" && ch <= "9") {
      count = count * 10 + Number(ch);
    } else if (ch === "[") {
      counts.push(count);
      outer.push(current);
      current = "";
      count = 0;
    } else if (ch === "]") {
      current = outer.pop()! + current.repeat(counts.pop()!);
    } else {
      current += ch;
    }
  }
  return current;
}

assert.equal(expandPatterns("3[ab]2[c]"), "abababcc");
assert.equal(expandPatterns("x2[y3[z]]"), "xyzzzyzzz");
assert.equal(expandPatterns(""), "");
assert.equal(expandPatterns("plain"), "plain");
assert.equal(expandPatterns("12[q]"), "qqqqqqqqqqqq");
assert.equal(expandPatterns("1[a1[b1[c]]]d"), "abcd");
assert.equal(expandPatterns("2[a2[b]c]"), "abbcabbc");
console.log("074 ok");
