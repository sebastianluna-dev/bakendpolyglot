// 038 · Parse an Integer Strictly
import assert from "node:assert/strict";

export function parseInteger(s: string): number | null {
  let i = 0;
  let negative = false;
  if (s[0] === "+" || s[0] === "-") {
    negative = s[0] === "-";
    i = 1;
  }
  if (i === s.length) return null; // empty, or a sign with no digits
  const limit = negative ? 2147483648 : 2147483647;
  let value = 0;
  for (; i < s.length; i++) {
    const ch = s[i];
    if (ch < "0" || ch > "9") return null;
    value = value * 10 + (ch.charCodeAt(0) - 48);
    if (value > limit) return null;
  }
  return negative ? 0 - value : value; // -value would turn "-0" into -0
}

assert.equal(parseInteger("42"), 42);
assert.equal(parseInteger("-17"), -17);
assert.equal(parseInteger("+8"), 8);
assert.equal(parseInteger("0042"), 42);
assert.equal(parseInteger("-0"), 0);
assert.equal(parseInteger("2147483647"), 2147483647);
assert.equal(parseInteger("-2147483648"), -2147483648);
assert.equal(parseInteger("2147483648"), null);
assert.equal(parseInteger("-2147483649"), null);
assert.equal(parseInteger("99999999999999999999"), null);
assert.equal(parseInteger(""), null);
assert.equal(parseInteger("+"), null);
assert.equal(parseInteger(" 42"), null);
assert.equal(parseInteger("42 "), null);
assert.equal(parseInteger("12a"), null);
assert.equal(parseInteger("+-3"), null);
assert.equal(parseInteger("3.0"), null);
console.log("038 ok");
