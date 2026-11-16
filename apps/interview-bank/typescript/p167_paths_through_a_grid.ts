// 167 · Paths Through a Grid
import assert from "node:assert/strict";

export function gridPaths(rows: number, cols: number): number {
  const row = new Array<number>(cols).fill(1); // the top row: one path to each cell
  for (let r = 1; r < rows; r++) {
    for (let c = 1; c < cols; c++) {
      row[c] += row[c - 1]; // from above (old row[c]) plus from the left
    }
  }
  return row[cols - 1];
}

assert.equal(gridPaths(3, 4), 10);
assert.equal(gridPaths(2, 2), 2);
assert.equal(gridPaths(1, 1), 1);
assert.equal(gridPaths(1, 5), 1);
assert.equal(gridPaths(5, 1), 1);
assert.equal(gridPaths(3, 7), 28);
assert.equal(gridPaths(10, 10), 48620);
assert.equal(gridPaths(17, 17), 601080390);
console.log("167 ok");
