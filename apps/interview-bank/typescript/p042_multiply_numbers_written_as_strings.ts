// 042 · Multiply Numbers Written as Strings
import assert from "node:assert/strict";

export function multiplyStrings(a: string, b: string): string {
  const digits = new Array<number>(a.length + b.length).fill(0);
  for (let i = a.length - 1; i >= 0; i--) {
    for (let j = b.length - 1; j >= 0; j--) {
      const sum = (a.charCodeAt(i) - 48) * (b.charCodeAt(j) - 48) + digits[i + j + 1];
      digits[i + j + 1] = sum % 10;
      digits[i + j] += Math.floor(sum / 10); // may pass 9 for now; fixed when its own turn comes
    }
  }
  let start = 0;
  while (start < digits.length - 1 && digits[start] === 0) start++;
  return digits.slice(start).join("");
}

assert.equal(multiplyStrings("123", "45"), "5535");
assert.equal(multiplyStrings("0", "9081"), "0");
assert.equal(multiplyStrings("999", "999"), "998001");
assert.equal(multiplyStrings("7", "8"), "56");
assert.equal(multiplyStrings("1", "1"), "1");
assert.equal(multiplyStrings("123456789", "987654321"), "121932631112635269");
assert.equal(
  multiplyStrings("99999999999999999999", "99999999999999999999"),
  "9999999999999999999800000000000000000001",
);
console.log("042 ok");
