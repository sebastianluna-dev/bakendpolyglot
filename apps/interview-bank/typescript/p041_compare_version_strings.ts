// 041 · Compare Version Strings
import assert from "node:assert/strict";

export function compareVersions(a: string, b: string): number {
  let i = 0;
  let j = 0;
  while (i < a.length || j < b.length) {
    let x = 0;
    while (i < a.length && a[i] !== ".") {
      x = x * 10 + (a.charCodeAt(i) - 48);
      i++;
    }
    let y = 0;
    while (j < b.length && b[j] !== ".") {
      y = y * 10 + (b.charCodeAt(j) - 48);
      j++;
    }
    if (x !== y) return x < y ? -1 : 1;
    i++; // step over the dot; past the end, the next part reads as 0
    j++;
  }
  return 0;
}

assert.equal(compareVersions("1.2.10", "1.2.9"), 1);
assert.equal(compareVersions("3.0.0", "3"), 0);
assert.equal(compareVersions("1.01", "1.001"), 0);
assert.equal(compareVersions("0.9", "1.0"), -1);
assert.equal(compareVersions("7.5.2.4", "7.5.3"), -1);
assert.equal(compareVersions("1.0.1", "1"), 1);
assert.equal(compareVersions("10.4", "9.12"), 1);
console.log("041 ok");
