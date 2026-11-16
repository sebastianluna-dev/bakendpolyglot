// 031 · Add Two Binary Strings
import assert from "node:assert/strict";

export function addBinary(a: string, b: string): string {
  const digits: number[] = [];
  let i = a.length - 1;
  let j = b.length - 1;
  let carry = 0;
  while (i >= 0 || j >= 0 || carry > 0) {
    let total = carry;
    if (i >= 0) total += a.charCodeAt(i--) - 48; // "0" is 48
    if (j >= 0) total += b.charCodeAt(j--) - 48;
    digits.push(total % 2);
    carry = Math.trunc(total / 2);
  }
  return digits.reverse().join("");
}

assert.equal(addBinary("1011", "111"), "10010");
assert.equal(addBinary("0", "0"), "0");
assert.equal(addBinary("1", "1"), "10");
assert.equal(addBinary("1111", "1"), "10000");
assert.equal(addBinary("100", "110010"), "110110");
assert.equal(addBinary("1".repeat(70), "1"), "1" + "0".repeat(70));
console.log("031 ok");
