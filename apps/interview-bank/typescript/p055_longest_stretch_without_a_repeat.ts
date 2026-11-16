// 055 · Longest Stretch Without a Repeat
import assert from "node:assert/strict";

export function longestWithoutRepeat(s: string): number {
  const last = new Map<string, number>(); // character -> index where it was last seen
  let start = 0;
  let best = 0;
  for (let i = 0; i < s.length; i++) {
    const prev = last.get(s[i]);
    if (prev !== undefined && prev >= start) start = prev + 1; // only a repeat inside the window counts
    last.set(s[i], i);
    best = Math.max(best, i - start + 1);
  }
  return best;
}

assert.equal(longestWithoutRepeat("shelfwise"), 7);
assert.equal(longestWithoutRepeat("aaaa"), 1);
assert.equal(longestWithoutRepeat(""), 0);
assert.equal(longestWithoutRepeat("abba"), 2);
assert.equal(longestWithoutRepeat("dvdf"), 3);
assert.equal(longestWithoutRepeat("A-1 a-2"), 5);
console.log("055 ok");
