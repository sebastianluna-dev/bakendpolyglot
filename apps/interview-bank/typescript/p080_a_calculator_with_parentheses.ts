// 080 · A Calculator With Parentheses
import assert from "node:assert/strict";

export function calculate(s: string): number {
  let result = 0;
  let sign = 1;
  let number = 0;
  const stack: number[] = []; // for each open bracket: the result before it, then the sign in front of it
  for (const ch of s) {
    if (ch >= "0" && ch <= "9") {
      number = number * 10 + Number(ch);
    } else if (ch === "+" || ch === "-") {
      result += sign * number;
      number = 0;
      sign = ch === "+" ? 1 : -1;
    } else if (ch === "(") {
      stack.push(result, sign);
      result = 0;
      sign = 1;
    } else if (ch === ")") {
      result += sign * number;
      number = 0;
      const outerSign = stack.pop()!;
      result = stack.pop()! + outerSign * result;
    }
  }
  return result + sign * number;
}

assert.equal(calculate("7 - (3 - (1 + 2))"), 7);
assert.equal(calculate("-(4 - (-2 + 9)) + 10"), 13);
assert.equal(calculate("12"), 12);
assert.equal(calculate(" 1 + 2 "), 3);
assert.equal(calculate("-5"), -5);
assert.equal(calculate("(((3)))"), 3);
assert.equal(calculate("10 - 2 - 3"), 5);
assert.equal(calculate("1 - (2 + 3)"), -4);
assert.equal(calculate("-(-(-1))"), -1);
assert.equal(calculate("2147483647"), 2147483647);
console.log("080 ok");
