// 053 · Three Readings That Sum to Zero
import assert from "node:assert/strict";

export function threeToZero(readings: number[]): number[][] {
  const s = readings.toSorted((a, b) => a - b);
  const result: number[][] = [];
  for (let i = 0; i < s.length - 2; i++) {
    if (i > 0 && s[i] === s[i - 1]) continue; // same first value, same triplets
    let lo = i + 1;
    let hi = s.length - 1;
    while (lo < hi) {
      const sum = s[i] + s[lo] + s[hi];
      if (sum < 0) lo++;
      else if (sum > 0) hi--;
      else {
        result.push([s[i], s[lo], s[hi]]);
        lo++;
        hi--;
        while (lo < hi && s[lo] === s[lo - 1]) lo++;
      }
    }
  }
  return result;
}

assert.deepEqual(threeToZero([-4, 1, 3, 0, -1, 2, -1]), [
  [-4, 1, 3],
  [-1, -1, 2],
  [-1, 0, 1],
]);
assert.deepEqual(threeToZero([0, 0, 0, 0]), [[0, 0, 0]]);
assert.deepEqual(threeToZero([1, 2, -4]), []);
assert.deepEqual(threeToZero([]), []);
assert.deepEqual(threeToZero([5, -5]), []);
assert.deepEqual(threeToZero([2, -2, 0, 2, 0]), [[-2, 0, 2]]);
console.log("053 ok");
