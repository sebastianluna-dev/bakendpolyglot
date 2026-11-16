// 028 · Longest Shared Prefix
import assert from "node:assert/strict";

export function longestSharedPrefix(words: string[]): string {
  if (words.length === 0) return "";
  const first = words[0];
  for (let i = 0; i < first.length; i++) {
    for (const word of words) {
      if (i === word.length || word[i] !== first[i]) return first.slice(0, i);
    }
  }
  return first;
}

assert.equal(longestSharedPrefix(["QA76.73", "QA76.76", "QA76.9"]), "QA76.");
assert.equal(longestSharedPrefix(["parcel", "pulse", "seat"]), "");
assert.equal(longestSharedPrefix([]), "");
assert.equal(longestSharedPrefix(["solo"]), "solo");
assert.equal(longestSharedPrefix(["ab", "a"]), "a");
assert.equal(longestSharedPrefix(["", "x"]), "");
assert.equal(longestSharedPrefix(["hold", "hold"]), "hold");
console.log("028 ok");
