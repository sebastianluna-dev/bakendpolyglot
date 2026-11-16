// 044 · Fully Justify Text
import assert from "node:assert/strict";

export function justifyText(words: string[], width: number): string[] {
  const lines: string[] = [];
  let i = 0;
  while (i < words.length) {
    let j = i + 1;
    let used = words[i].length; // the words plus one space between each pair
    while (j < words.length && used + 1 + words[j].length <= width) {
      used += 1 + words[j].length;
      j++;
    }
    const gaps = j - i - 1;
    if (j === words.length || gaps === 0) {
      lines.push(words.slice(i, j).join(" ").padEnd(width));
    } else {
      const spaces = width - (used - gaps);
      const each = Math.floor(spaces / gaps);
      const extra = spaces % gaps;
      const parts: string[] = [];
      for (let k = i; k < j - 1; k++) {
        parts.push(words[k], " ".repeat(each + (k - i < extra ? 1 : 0)));
      }
      parts.push(words[j - 1]);
      lines.push(parts.join(""));
    }
    i = j;
  }
  return lines;
}

assert.deepEqual(justifyText(["Your", "loan", "of", "Dune", "is", "due", "on", "Friday."], 16), [
  "Your   loan   of",
  "Dune  is  due on",
  "Friday.         ",
]);
assert.deepEqual(justifyText(["Hold", "expires", "in", "ten", "minutes"], 10), [
  "Hold      ",
  "expires in",
  "ten       ",
  "minutes   ",
]);
assert.deepEqual(justifyText(["to", "be", "or", "not"], 6), ["to  be", "or not"]);
assert.deepEqual(justifyText(["exact"], 5), ["exact"]);
assert.deepEqual(justifyText([], 8), []);
console.log("044 ok");
