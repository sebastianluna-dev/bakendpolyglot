// 138 · Order of an Unknown Alphabet
import assert from "node:assert/strict";

export function alphabetOrder(words: string[]): string {
  const present = new Array<boolean>(26).fill(false);
  for (const w of words) for (let k = 0; k < w.length; k++) present[w.charCodeAt(k) - 97] = true;
  const before = Array.from({ length: 26 }, () => new Array<boolean>(26).fill(false)); // before[x][y]: x comes before y
  const waiting = new Array<number>(26).fill(0); // letters that must come before this one and are not placed yet
  for (let i = 0; i + 1 < words.length; i++) {
    const a = words[i];
    const b = words[i + 1];
    let j = 0;
    while (j < a.length && j < b.length && a[j] === b[j]) j++;
    if (j === a.length || j === b.length) {
      if (a.length > b.length) return ""; // a word sorted before its own prefix
      continue;
    }
    const x = a.charCodeAt(j) - 97;
    const y = b.charCodeAt(j) - 97;
    if (!before[x][y]) {
      before[x][y] = true;
      waiting[y]++;
    }
  }
  const placed = new Array<boolean>(26).fill(false);
  let order = "";
  while (true) {
    let x = 0;
    while (x < 26 && !(present[x] && !placed[x] && waiting[x] === 0)) x++;
    if (x === 26) break;
    placed[x] = true;
    order += String.fromCharCode(97 + x);
    for (let y = 0; y < 26; y++) if (before[x][y]) waiting[y]--;
  }
  return order.length === present.filter(Boolean).length ? order : ""; // shorter means a cycle
}

assert.equal(alphabetOrder(["cab", "cad", "ab", "ba", "bd"]), "cabd");
assert.equal(alphabetOrder(["ba", "bc", "ad"]), "bacd");
assert.equal(alphabetOrder(["abc", "ab"]), "");
assert.equal(alphabetOrder(["a", "b", "a"]), "");
assert.equal(alphabetOrder(["a", "b", "c", "a"]), "");
assert.equal(alphabetOrder(["zyx"]), "xyz");
assert.equal(alphabetOrder(["ab", "ab"]), "ab");
assert.equal(alphabetOrder(["ab", "abc"]), "abc");
assert.equal(alphabetOrder(["x", "z", "y"]), "xzy");
console.log("138 ok");
