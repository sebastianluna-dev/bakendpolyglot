// 168 · Paint the Houses for Less
import assert from "node:assert/strict";

export function minPaintCost(costs: number[][]): number {
  let red = 0; // cheapest total so far if the last house painted is red
  let green = 0;
  let blue = 0;
  for (const [r, g, b] of costs) {
    [red, green, blue] = [r + Math.min(green, blue), g + Math.min(red, blue), b + Math.min(red, green)];
  }
  return Math.min(red, green, blue);
}

assert.equal(minPaintCost([[8, 3, 6], [4, 9, 2], [7, 5, 1]]), 8);
assert.equal(minPaintCost([[1, 2, 9], [1, 9, 9]]), 3);
assert.equal(minPaintCost([]), 0);
assert.equal(minPaintCost([[5, 1, 9]]), 1);
assert.equal(minPaintCost([[7, 6, 2], [3, 8, 9], [4, 1, 6], [9, 2, 3]]), 9);
assert.equal(minPaintCost([[4, 4, 4], [4, 4, 4]]), 8);
console.log("168 ok");
