// 072 · Evaluate Postfix Expressions
import assert from "node:assert/strict";

export function evaluatePostfix(tokens: string[]): number {
  const stack: number[] = [];
  for (const token of tokens) {
    if (token === "+" || token === "-" || token === "*" || token === "/") {
      const b = stack.pop()!;
      const a = stack.pop()!;
      if (token === "+") stack.push(a + b);
      else if (token === "-") stack.push(a - b);
      else if (token === "*") stack.push(a * b);
      else stack.push(Math.trunc(a / b) + 0); // + 0 turns -0 into 0
    } else {
      stack.push(Number(token));
    }
  }
  return stack[stack.length - 1];
}

assert.equal(evaluatePostfix(["4", "13", "5", "/", "+"]), 6);
assert.equal(evaluatePostfix(["-7", "2", "/", "3", "*"]), -9);
assert.equal(evaluatePostfix(["42"]), 42);
assert.equal(evaluatePostfix(["8", "2", "/", "3", "-"]), 1);
assert.equal(evaluatePostfix(["2", "-5", "/"]), 0);
assert.equal(evaluatePostfix(["-9", "-2", "/"]), 4);
assert.equal(evaluatePostfix(["5", "1", "2", "+", "4", "*", "+", "3", "-"]), 14);
console.log("072 ok");
