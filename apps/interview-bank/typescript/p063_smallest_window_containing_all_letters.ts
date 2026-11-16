// 063 · Smallest Window Containing All Letters
import assert from "node:assert/strict";

export function smallestWindow(text: string, letters: string): string {
  const need = new Array<number>(128).fill(0); // copies still wanted, by character code
  for (let i = 0; i < letters.length; i++) need[letters.charCodeAt(i)]++;
  let missing = letters.length;
  let bestStart = 0;
  let bestLength = text.length + 1;
  let left = 0;
  for (let right = 0; right < text.length; right++) {
    const c = text.charCodeAt(right);
    if (need[c] > 0) missing--;
    need[c]--;
    while (missing === 0) {
      if (right - left + 1 < bestLength) {
        bestStart = left;
        bestLength = right - left + 1;
      }
      const d = text.charCodeAt(left);
      need[d]++;
      if (need[d] > 0) missing++; // a wanted copy just left the window
      left++;
    }
  }
  return bestLength > text.length ? "" : text.slice(bestStart, bestStart + bestLength);
}

assert.equal(smallestWindow("deliverytruck", "tire"), "iveryt");
assert.equal(smallestWindow("banana", "nn"), "nan");
assert.equal(smallestWindow("abba", "ab"), "ab");
assert.equal(smallestWindow("abc", "abd"), "");
assert.equal(smallestWindow("a", "aa"), "");
assert.equal(smallestWindow("Aa", "a"), "a");
assert.equal(smallestWindow("", "a"), "");
assert.equal(smallestWindow("parcel", "parcel"), "parcel");
console.log("063 ok");
