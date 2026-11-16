// 172 · Can the Text Be Split Into Words?
import assert from "node:assert/strict";

export function canSplit(text: string, words: string[]): boolean {
  const dictionary = new Set(words);
  let longest = 0;
  for (const w of words) longest = Math.max(longest, w.length);
  const splits = new Array<boolean>(text.length + 1).fill(false); // splits[i]: the first i letters split
  splits[0] = true;
  for (let end = 1; end <= text.length; end++) {
    for (let len = 1; len <= Math.min(end, longest); len++) {
      if (splits[end - len] && dictionary.has(text.slice(end - len, end))) {
        splits[end] = true;
        break;
      }
    }
  }
  return splits[text.length];
}

assert.equal(canSplit("bookseller", ["books", "book", "seller"]), true);
assert.equal(canSplit("trucking", ["truck", "king", "in"]), false);
assert.equal(canSplit("", ["a"]), true);
assert.equal(canSplit("abcd", []), false);
assert.equal(canSplit("seatseat", ["seat"]), true);
assert.equal(canSplit("aaaaaaa", ["aaaa", "aaa"]), true);
assert.equal(canSplit("aaaaaaaaab", ["a", "aa"]), false);
console.log("172 ok");
