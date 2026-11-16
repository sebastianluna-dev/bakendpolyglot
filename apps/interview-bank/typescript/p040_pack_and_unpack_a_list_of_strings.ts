// 040 · Pack and Unpack a List of Strings
import assert from "node:assert/strict";

export function pack(words: string[]): string {
  const parts: string[] = [];
  for (const w of words) parts.push(String(w.length), "#", w);
  return parts.join("");
}

export function unpack(packed: string): string[] {
  const words: string[] = [];
  let i = 0;
  while (i < packed.length) {
    let length = 0;
    while (packed[i] !== "#") {
      length = length * 10 + (packed.charCodeAt(i) - 48);
      i++;
    }
    i++; // skip the "#"
    words.push(packed.slice(i, i + length));
    i += length;
  }
  return words;
}

const cases: [string[], string][] = [
  [["truck 7", "", "door#2"], "7#truck 70#6#door#2"],
  [["parcel-12345"], "12#parcel-12345"],
  [[], ""],
  [[""], "0#"],
  [["#", "12#", "a b", "3#x"], "1##3#12#3#a b3#3#x"],
];
for (const [words, packed] of cases) {
  assert.equal(pack(words), packed);
  assert.deepEqual(unpack(packed), words);
}
console.log("040 ok");
