// 033 · Is It a Rotation?
import assert from "node:assert/strict";

export function isRotation(a: string, b: string): boolean {
  return a.length === b.length && (a + a).includes(b);
}

assert.equal(isRotation("parcel", "celpar"), true);
assert.equal(isRotation("parcel", "parcle"), false);
assert.equal(isRotation("abc", "abc"), true);
assert.equal(isRotation("", ""), true);
assert.equal(isRotation("aa", "a"), false);
assert.equal(isRotation("abab", "baba"), true);
console.log("033 ok");
