// 030 · Letter-for-Letter Mapping
import assert from "node:assert/strict";

export function canMapLetters(source: string, target: string): boolean {
  if (source.length !== target.length) return false;
  const forward = new Map<string, string>();
  const backward = new Map<string, string>(); // stops two letters mapping to the same one
  for (let i = 0; i < source.length; i++) {
    const a = source[i];
    const b = target[i];
    if ((forward.get(a) ?? b) !== b || (backward.get(b) ?? a) !== a) return false;
    forward.set(a, b);
    backward.set(b, a);
  }
  return true;
}

assert.equal(canMapLetters("deed", "noon"), true);
assert.equal(canMapLetters("bell", "cool"), false);
assert.equal(canMapLetters("", ""), true);
assert.equal(canMapLetters("a", "bc"), false);
assert.equal(canMapLetters("abc", "bca"), true);
assert.equal(canMapLetters("ab", "aa"), false);
assert.equal(canMapLetters("aa", "ab"), false);
assert.equal(canMapLetters("tab", "tat"), false);
console.log("030 ok");
