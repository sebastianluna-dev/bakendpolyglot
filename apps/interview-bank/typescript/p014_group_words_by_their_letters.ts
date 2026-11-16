// 014 · Group Words by Their Letters
import assert from "node:assert/strict";

export function groupByLetters(words: string[]): string[][] {
  const groupOf = new Map<string, number>(); // sorted letters -> index in groups
  const groups: string[][] = [];
  for (const word of words) {
    const key = [...word].sort().join("");
    let g = groupOf.get(key);
    if (g === undefined) {
      g = groups.length;
      groupOf.set(key, g);
      groups.push([]);
    }
    groups[g].push(word);
  }
  return groups;
}

assert.deepEqual(groupByLetters(["listen", "stone", "silent", "notes", "enlist", "tones", "tab"]), [
  ["listen", "silent", "enlist"],
  ["stone", "notes", "tones"],
  ["tab"],
]);
assert.deepEqual(groupByLetters(["loop", "pool", "polo", "lop"]), [["loop", "pool", "polo"], ["lop"]]);
assert.deepEqual(groupByLetters([]), []);
assert.deepEqual(groupByLetters(["ab", "ba", "ab"]), [["ab", "ba", "ab"]]);
assert.deepEqual(groupByLetters(["", "a", ""]), [["", ""], ["a"]]);
console.log("014 ok");
