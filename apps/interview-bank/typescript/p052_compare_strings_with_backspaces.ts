// 052 · Compare Strings With Backspaces
import assert from "node:assert/strict";

export function sameAfterBackspaces(a: string, b: string): boolean {
  let i = a.length - 1;
  let j = b.length - 1;
  while (i >= 0 || j >= 0) {
    i = lastKept(a, i);
    j = lastKept(b, j);
    if (i < 0 || j < 0) return i < 0 && j < 0;
    if (a[i] !== b[j]) return false;
    i--;
    j--;
  }
  return true;
}

// Index of the last character at or before i that no later '#' deletes, or -1.
function lastKept(s: string, i: number): number {
  let skip = 0;
  while (i >= 0) {
    if (s[i] === "#") skip++;
    else if (skip > 0) skip--;
    else break;
    i--;
  }
  return i;
}

assert.equal(sameAfterBackspaces("A1#2", "A2"), true);
assert.equal(sameAfterBackspaces("B7##C", "C#B"), false);
assert.equal(sameAfterBackspaces("##", ""), true);
assert.equal(sameAfterBackspaces("ab##", "c#d#"), true);
assert.equal(sameAfterBackspaces("x#y", "y"), true);
assert.equal(sameAfterBackspaces("abc", "ab"), false);
assert.equal(sameAfterBackspaces("a", "ba"), false);
assert.equal(sameAfterBackspaces("a#b#c", "#c"), true);
console.log("052 ok");
