// 066 · Balanced Brackets
import assert from "node:assert/strict";

const CLOSER = new Map([["(", ")"], ["[", "]"], ["{", "}"]]);

export function isBalanced(s: string): boolean {
  const expected: string[] = []; // the closer each open bracket is waiting for
  for (const ch of s) {
    const closer = CLOSER.get(ch);
    if (closer !== undefined) expected.push(closer);
    else if (ch === ")" || ch === "]" || ch === "}") {
      if (expected.pop() !== ch) return false;
    }
  }
  return expected.length === 0;
}

assert.equal(isBalanced("lookup(ids[0], {limit: 5})"), true);
assert.equal(isBalanced("([)]"), false);
assert.equal(isBalanced(""), true);
assert.equal(isBalanced("no brackets"), true);
assert.equal(isBalanced("("), false);
assert.equal(isBalanced(")"), false);
assert.equal(isBalanced("}{"), false);
assert.equal(isBalanced("(("), false);
assert.equal(isBalanced("{[()()]}"), true);
console.log("066 ok");
