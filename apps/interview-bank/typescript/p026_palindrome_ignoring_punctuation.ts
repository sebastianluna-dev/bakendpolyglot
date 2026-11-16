// 026 · Palindrome, Ignoring Punctuation
import assert from "node:assert/strict";

export function isPalindrome(text: string): boolean {
  let left = 0;
  let right = text.length - 1;
  while (left < right) {
    if (!isLetterOrDigit(text[left])) left++;
    else if (!isLetterOrDigit(text[right])) right--;
    else if (text[left].toLowerCase() !== text[right].toLowerCase()) return false;
    else {
      left++;
      right--;
    }
  }
  return true;
}

function isLetterOrDigit(ch: string): boolean {
  return /[a-z0-9]/i.test(ch);
}

assert.equal(isPalindrome("Step on no pets!"), true);
assert.equal(isPalindrome("Pets on no steps"), false);
assert.equal(isPalindrome(""), true);
assert.equal(isPalindrome(",.! ?"), true);
assert.equal(isPalindrome("a."), true);
assert.equal(isPalindrome("Ab1bA"), true);
assert.equal(isPalindrome("1a2"), false);
assert.equal(isPalindrome("0P"), false);
console.log("026 ok");
