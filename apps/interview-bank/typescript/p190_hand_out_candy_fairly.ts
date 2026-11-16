// 190 · Hand Out Candy Fairly
import assert from "node:assert/strict";

export function minCandy(scores: number[]): number {
  const n = scores.length;
  const candy = new Array<number>(n).fill(1);
  for (let i = 1; i < n; i++) {
    if (scores[i] > scores[i - 1]) candy[i] = candy[i - 1] + 1;
  }
  for (let i = n - 2; i >= 0; i--) {
    // max keeps what the first pass gave for the left neighbour
    if (scores[i] > scores[i + 1]) candy[i] = Math.max(candy[i], candy[i + 1] + 1);
  }
  return candy.reduce((sum, c) => sum + c, 0);
}

assert.equal(minCandy([1, 3, 2, 2, 1]), 7);
assert.equal(minCandy([5, 4, 3, 4]), 8);
assert.equal(minCandy([]), 0);
assert.equal(minCandy([7]), 1);
assert.equal(minCandy([2, 2, 2]), 3);
assert.equal(minCandy([1, 2, 3, 4]), 10);
assert.equal(minCandy([1, 2, 3, 4, 1]), 11);
assert.equal(minCandy([4, 3, 2, 1]), 10);
console.log("190 ok");
