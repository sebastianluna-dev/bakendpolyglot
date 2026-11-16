// 021 · Read a Matrix in a Spiral
import assert from "node:assert/strict";

export function spiralOrder(grid: number[][]): number[] {
  const result: number[] = [];
  if (grid.length === 0) return result;
  let top = 0;
  let bottom = grid.length - 1;
  let left = 0;
  let right = grid[0].length - 1;
  while (top <= bottom && left <= right) {
    for (let c = left; c <= right; c++) result.push(grid[top][c]);
    top++;
    for (let r = top; r <= bottom; r++) result.push(grid[r][right]);
    right--;
    // after the two steps above, a single row or column may be used up
    if (top <= bottom) {
      for (let c = right; c >= left; c--) result.push(grid[bottom][c]);
      bottom--;
    }
    if (left <= right) {
      for (let r = bottom; r >= top; r--) result.push(grid[r][left]);
      left++;
    }
  }
  return result;
}

assert.deepEqual(spiralOrder([[1, 2, 3], [8, 9, 4], [7, 6, 5]]), [1, 2, 3, 4, 5, 6, 7, 8, 9]);
assert.deepEqual(
  spiralOrder([[10, 11, 12, 13], [20, 21, 22, 23], [30, 31, 32, 33]]),
  [10, 11, 12, 13, 23, 33, 32, 31, 30, 20, 21, 22],
);
assert.deepEqual(spiralOrder([]), []);
assert.deepEqual(spiralOrder([[4, 5, 6]]), [4, 5, 6]);
assert.deepEqual(spiralOrder([[4], [5], [6]]), [4, 5, 6]);
assert.deepEqual(spiralOrder([[1, 2], [3, 4], [5, 6]]), [1, 2, 4, 6, 5, 3]);
console.log("021 ok");
