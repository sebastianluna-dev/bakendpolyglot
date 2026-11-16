// 157 · Every Balanced Bracket String
import assert from "node:assert/strict";

export function balancedBrackets(n: number): string[] {
  const result: string[] = [];
  const path: string[] = [];
  function walk(opened: number, closed: number): void {
    if (closed === n) {
      result.push(path.join(""));
      return;
    }
    if (opened < n) {
      path.push("(");
      walk(opened + 1, closed);
      path.pop();
    }
    if (closed < opened) {
      path.push(")");
      walk(opened, closed + 1);
      path.pop();
    }
  }
  walk(0, 0);
  return result;
}

assert.deepEqual(balancedBrackets(3), ["((()))", "(()())", "(())()", "()(())", "()()()"]);
assert.deepEqual(balancedBrackets(1), ["()"]);
assert.deepEqual(balancedBrackets(0), [""]);
assert.deepEqual(balancedBrackets(2), ["(())", "()()"]);
assert.equal(balancedBrackets(4).length, 14);
console.log("157 ok");
