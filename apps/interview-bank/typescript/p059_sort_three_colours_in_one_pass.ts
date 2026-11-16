// 059 · Sort Three Colours in One Pass
import assert from "node:assert/strict";

export function sortColours(stickers: number[]): void {
  let low = 0; // everything before low is 0
  let mid = 0; // everything from low to mid - 1 is 1
  let high = stickers.length - 1; // everything after high is 2
  while (mid <= high) {
    if (stickers[mid] === 0) {
      [stickers[low], stickers[mid]] = [stickers[mid], stickers[low]];
      low++;
      mid++;
    } else if (stickers[mid] === 1) {
      mid++;
    } else {
      [stickers[mid], stickers[high]] = [stickers[high], stickers[mid]];
      high--; // mid stays: the value swapped in has not been looked at yet
    }
  }
}

const cases: [number[], number[]][] = [
  [[2, 0, 1, 2, 1, 0], [0, 0, 1, 1, 2, 2]],
  [[1, 1, 1], [1, 1, 1]],
  [[], []],
  [[2, 0], [0, 2]],
  [[2, 2, 0, 0], [0, 0, 2, 2]],
  [[0, 2, 1], [0, 1, 2]],
  [[1, 2, 0], [0, 1, 2]],
  [[1, 2, 0, 2, 0, 1, 0], [0, 0, 0, 1, 1, 2, 2]],
];
for (const [stickers, want] of cases) {
  sortColours(stickers);
  assert.deepEqual(stickers, want);
}
console.log("059 ok");
