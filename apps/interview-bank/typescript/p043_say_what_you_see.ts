// 043 · Say What You See
import assert from "node:assert/strict";

export function sayWhatYouSee(start: string, rounds: number): string {
  let s = start;
  for (let r = 0; r < rounds; r++) {
    const parts: string[] = [];
    let i = 0;
    while (i < s.length) {
      let j = i;
      while (j < s.length && s[j] === s[i]) j++;
      parts.push(String(j - i) + s[i]);
      i = j;
    }
    s = parts.join("");
  }
  return s;
}

assert.equal(sayWhatYouSee("1", 4), "111221");
assert.equal(sayWhatYouSee("3322251", 1), "23321511");
assert.equal(sayWhatYouSee("1", 0), "1");
assert.equal(sayWhatYouSee("22", 5), "22");
assert.equal(sayWhatYouSee("1111111111", 1), "101");
assert.equal(sayWhatYouSee("1", 9), "13211311123113112211");
console.log("043 ok");
