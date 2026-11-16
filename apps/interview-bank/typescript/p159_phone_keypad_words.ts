// 159 · Phone Keypad Words
import assert from "node:assert/strict";

const KEYS = ["", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"];

export function keypadWords(digits: string): string[] {
  if (digits.length === 0) return [];
  const result: string[] = [];
  const path: string[] = [];
  function walk(k: number): void {
    if (k === digits.length) {
      result.push(path.join(""));
      return;
    }
    for (const letter of KEYS[digits.charCodeAt(k) - 48]) {
      path.push(letter);
      walk(k + 1);
      path.pop();
    }
  }
  walk(0);
  return result;
}

assert.deepEqual(keypadWords("23"), ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"]);
assert.deepEqual(keypadWords("7"), ["p", "q", "r", "s"]);
assert.deepEqual(keypadWords(""), []);
assert.deepEqual(keypadWords("9"), ["w", "x", "y", "z"]);
const words = keypadWords("79");
assert.equal(words.length, 16);
assert.equal(words[0], "pw");
assert.equal(words.at(-1), "sz");
assert.equal(keypadWords("234").length, 27);
console.log("159 ok");
