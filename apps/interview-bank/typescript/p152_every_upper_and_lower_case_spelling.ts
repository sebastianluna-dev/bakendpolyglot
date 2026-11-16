// 152 · Every Upper- and Lower-Case Spelling
import assert from "node:assert/strict";

export function allSpellings(code: string): string[] {
  const chars = code.split("");
  const out: string[] = [];
  function spell(i: number): void {
    if (i === chars.length) {
      out.push(chars.join(""));
      return;
    }
    const c = code[i];
    if (!/[a-zA-Z]/.test(c)) { // a digit: one choice
      spell(i + 1);
      return;
    }
    chars[i] = c.toUpperCase(); // upper case first keeps the output sorted
    spell(i + 1);
    chars[i] = c.toLowerCase();
    spell(i + 1);
  }
  spell(0);
  return out;
}

assert.deepEqual(allSpellings("b7x"), ["B7X", "B7x", "b7X", "b7x"]);
assert.deepEqual(allSpellings("42"), ["42"]);
assert.deepEqual(allSpellings("Q"), ["Q", "q"]);
assert.deepEqual(allSpellings("aB1"), ["AB1", "Ab1", "aB1", "ab1"]);
assert.deepEqual(allSpellings("0z0"), ["0Z0", "0z0"]);
assert.equal(allSpellings("abcdefghij").length, 1024);
console.log("152 ok");
