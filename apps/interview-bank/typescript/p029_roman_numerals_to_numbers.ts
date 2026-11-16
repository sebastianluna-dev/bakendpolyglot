// 029 · Roman Numerals to Numbers
import assert from "node:assert/strict";

const VALUES = new Map<string, number>([
  ["I", 1], ["V", 5], ["X", 10], ["L", 50], ["C", 100], ["D", 500], ["M", 1000],
]);

export function romanToNumber(roman: string): number {
  let total = 0;
  for (let i = 0; i < roman.length; i++) {
    const value = VALUES.get(roman[i])!;
    const next = i + 1 < roman.length ? VALUES.get(roman[i + 1])! : 0;
    // a smaller symbol before a larger one is subtracted (IV = 5 - 1)
    total += value < next ? -value : value;
  }
  return total;
}

assert.equal(romanToNumber("MMXXVI"), 2026);
assert.equal(romanToNumber("CDXLIV"), 444);
assert.equal(romanToNumber("I"), 1);
assert.equal(romanToNumber("IV"), 4);
assert.equal(romanToNumber("IX"), 9);
assert.equal(romanToNumber("XIV"), 14);
assert.equal(romanToNumber("CM"), 900);
assert.equal(romanToNumber("MMMCMXCIX"), 3999);
console.log("029 ok");
