// 037 · Numbers to Roman Numerals
import assert from "node:assert/strict";

const VALUES = [1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1];
const SYMBOLS = ["M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"];

export function toRoman(n: number): string {
  const parts: string[] = [];
  for (let i = 0; i < VALUES.length; i++) {
    while (n >= VALUES[i]) {
      parts.push(SYMBOLS[i]);
      n -= VALUES[i];
    }
  }
  return parts.join("");
}

assert.equal(toRoman(2026), "MMXXVI");
assert.equal(toRoman(1994), "MCMXCIV");
assert.equal(toRoman(1), "I");
assert.equal(toRoman(4), "IV");
assert.equal(toRoman(49), "XLIX");
assert.equal(toRoman(58), "LVIII");
assert.equal(toRoman(444), "CDXLIV");
assert.equal(toRoman(3999), "MMMCMXCIX");
console.log("037 ok");
