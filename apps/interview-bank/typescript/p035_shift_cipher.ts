// 035 · Shift Cipher
import assert from "node:assert/strict";

export function shiftCipher(text: string, shift: number): string {
  const k = ((shift % 26) + 26) % 26; // % keeps the sign of shift, so -1 must become 25
  const chars = [...text];
  for (let i = 0; i < chars.length; i++) {
    const ch = chars[i];
    if (ch >= "a" && ch <= "z") {
      chars[i] = String.fromCharCode(97 + ((ch.charCodeAt(0) - 97 + k) % 26));
    } else if (ch >= "A" && ch <= "Z") {
      chars[i] = String.fromCharCode(65 + ((ch.charCodeAt(0) - 65 + k) % 26));
    }
  }
  return chars.join("");
}

assert.equal(shiftCipher("Hold seat 14!", 3), "Krog vhdw 14!");
assert.equal(shiftCipher("xyz", 2), "zab");
assert.equal(shiftCipher("Abc", -1), "Zab");
assert.equal(shiftCipher("abc", 52), "abc");
assert.equal(shiftCipher("Zz", -27), "Yy");
assert.equal(shiftCipher("Krog vhdw 14!", -3), "Hold seat 14!");
assert.equal(shiftCipher("", 5), "");
console.log("035 ok");
