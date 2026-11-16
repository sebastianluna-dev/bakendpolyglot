// 127 · Perimeter of an Island
import assert from "node:assert/strict";

export function islandPerimeter(grid: number[][]): number {
  let perimeter = 0;
  for (let r = 0; r < grid.length; r++) {
    for (let c = 0; c < grid[r].length; c++) {
      if (grid[r][c] !== 1) continue;
      perimeter += 4;
      if (r > 0 && grid[r - 1][c] === 1) perimeter -= 2; // the shared side is lost by both cells
      if (c > 0 && grid[r][c - 1] === 1) perimeter -= 2;
    }
  }
  return perimeter;
}

assert.equal(islandPerimeter([[0, 1, 0], [1, 1, 1], [0, 1, 0]]), 12);
assert.equal(islandPerimeter([[1, 1], [1, 1]]), 8);
assert.equal(islandPerimeter([[1]]), 4);
assert.equal(islandPerimeter([[0, 0, 0], [0, 1, 0]]), 4);
assert.equal(islandPerimeter([[1, 1, 1, 0]]), 8);
assert.equal(islandPerimeter([[1, 1, 1], [1, 0, 1], [1, 1, 1]]), 16);
console.log("127 ok");
