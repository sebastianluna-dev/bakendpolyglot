// 135 · Shortest Clear Path in a Grid
import assert from "node:assert/strict";

export function shortestClearPath(grid: number[][]): number {
  const rows = grid.length;
  const cols = grid[0].length;
  if (grid[0][0] === 1 || grid[rows - 1][cols - 1] === 1) return -1;
  const cells = Array.from({ length: rows }, () => new Array<number>(cols).fill(0)); // 0 = not reached
  cells[0][0] = 1;
  const queue: [number, number][] = [[0, 0]];
  let head = 0;
  while (head < queue.length) {
    const [r, c] = queue[head++];
    if (r === rows - 1 && c === cols - 1) return cells[r][c];
    for (let dr = -1; dr <= 1; dr++) {
      for (let dc = -1; dc <= 1; dc++) {
        const nr = r + dr;
        const nc = c + dc;
        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols) continue;
        if (grid[nr][nc] === 1 || cells[nr][nc] !== 0) continue; // also skips (r, c) itself
        cells[nr][nc] = cells[r][c] + 1;
        queue.push([nr, nc]);
      }
    }
  }
  return -1;
}

assert.equal(shortestClearPath([[0, 0, 0], [1, 1, 0], [1, 1, 0]]), 4);
assert.equal(shortestClearPath([[0, 1], [1, 0]]), 2);
assert.equal(shortestClearPath([[0]]), 1);
assert.equal(shortestClearPath([[1]]), -1);
assert.equal(shortestClearPath([[1, 0], [0, 0]]), -1);
assert.equal(shortestClearPath([[0, 0], [0, 1]]), -1);
assert.equal(shortestClearPath([[0, 1, 0], [1, 1, 0], [0, 0, 0]]), -1);
assert.equal(shortestClearPath([[0, 0, 0, 0]]), 4);
assert.equal(shortestClearPath([[0, 1, 0, 0], [0, 1, 0, 1], [0, 1, 0, 1], [1, 0, 1, 0]]), 6);
console.log("135 ok");
