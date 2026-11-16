// 078 · Remove k Digits for the Smallest Number
import assert from "node:assert/strict";

export function removeKDigits(num: string, k: number): string {
  const kept: string[] = [];
  for (const d of num) {
    while (k > 0 && kept.length > 0 && kept[kept.length - 1] > d) {
      kept.pop();
      k--;
    }
    kept.push(d);
  }
  kept.length -= k; // digits never fell, so drop the rest from the end
  let start = 0;
  while (start < kept.length - 1 && kept[start] === "0") start++;
  return kept.length === 0 ? "0" : kept.slice(start).join("");
}

assert.equal(removeKDigits("4172", 2), "12");
assert.equal(removeKDigits("30200", 1), "200");
assert.equal(removeKDigits("9", 1), "0");
assert.equal(removeKDigits("12345", 2), "123");
assert.equal(removeKDigits("10", 1), "0");
assert.equal(removeKDigits("1432219", 0), "1432219");
assert.equal(removeKDigits("112", 1), "11");
assert.equal(removeKDigits("100200", 1), "200");
console.log("078 ok");
