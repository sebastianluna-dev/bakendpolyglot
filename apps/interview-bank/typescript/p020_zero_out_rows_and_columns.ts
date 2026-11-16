// 020 · Zero Out Rows and Columns
import assert from "node:assert/strict";

export function zeroOut(grid: number[][]): void {
  const zeroRow = new Array<boolean>(grid.length).fill(false);
  const zeroCol = new Array<boolean>(grid[0].length).fill(false);
  for (let r = 0; r < grid.length; r++) {
    for (let c = 0; c < grid[0].length; c++) {
      if (grid[r][c] === 0) zeroRow[r] = zeroCol[c] = true;
    }
  }
  for (let r = 0; r < grid.length; r++) {
    for (let c = 0; c < grid[0].length; c++) {
      if (zeroRow[r] || zeroCol[c]) grid[r][c] = 0;
    }
  }
}

function zeroed(grid: number[][]): number[][] {
  zeroOut(grid);
  return grid;
}

assert.deepEqual(zeroed([[1, 2, 3], [4, 0, 6], [7, 8, 9]]), [[1, 0, 3], [0, 0, 0], [7, 0, 9]]);
assert.deepEqual(zeroed([[0, 1, 2, 0], [3, 4, 5, 6], [1, 3, 1, 5]]), [[0, 0, 0, 0], [0, 4, 5, 0], [0, 3, 1, 0]]);
assert.deepEqual(zeroed([[0, 1], [1, 1]]), [[0, 0], [0, 1]]);
assert.deepEqual(zeroed([[1, 2], [3, 4]]), [[1, 2], [3, 4]]);
assert.deepEqual(zeroed([[0]]), [[0]]);
assert.deepEqual(zeroed([[5, 0, 7]]), [[0, 0, 0]]);
console.log("020 ok");
