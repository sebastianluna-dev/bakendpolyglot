// 153 · Binary Strings With No Adjacent Ones
import assert from "node:assert/strict";

export function noAdjacentOnes(n: number): string[] {
  const result: string[] = [];
  const path: string[] = [];
  function walk(): void {
    if (path.length === n) {
      result.push(path.join(""));
      return;
    }
    path.push("0");
    walk();
    path.pop();
    if (path.length === 0 || path.at(-1) === "0") {
      path.push("1");
      walk();
      path.pop();
    }
  }
  walk();
  return result;
}

assert.deepEqual(noAdjacentOnes(3), ["000", "001", "010", "100", "101"]);
assert.deepEqual(noAdjacentOnes(1), ["0", "1"]);
assert.deepEqual(noAdjacentOnes(0), [""]);
assert.deepEqual(noAdjacentOnes(2), ["00", "01", "10"]);
assert.deepEqual(noAdjacentOnes(4), ["0000", "0001", "0010", "0100", "0101", "1000", "1001", "1010"]);
assert.equal(noAdjacentOnes(10).length, 144);
console.log("153 ok");
