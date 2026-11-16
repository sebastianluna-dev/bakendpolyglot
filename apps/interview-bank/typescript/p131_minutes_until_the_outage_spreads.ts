// 131 · Minutes Until the Outage Spreads
import assert from "node:assert/strict";

export function minutesUntilAllFail(grid: number[][]): number {
  const rows = grid.length;
  const cols = grid[0].length;
  const dirs = [[1, 0], [-1, 0], [0, 1], [0, -1]];
  const queue: [number, number][] = [];
  let head = 0;
  let healthy = 0;
  for (let r = 0; r < rows; r++) {
    for (let c = 0; c < cols; c++) {
      if (grid[r][c] === 2) queue.push([r, c]);
      else if (grid[r][c] === 1) healthy++;
    }
  }
  let minutes = 0;
  while (head < queue.length && healthy > 0) {
    minutes++;
    const levelEnd = queue.length; // one minute: only the servers that failed last minute
    while (head < levelEnd) {
      const [r, c] = queue[head++];
      for (const [dr, dc] of dirs) {
        const nr = r + dr;
        const nc = c + dc;
        if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] === 1) {
          grid[nr][nc] = 2;
          healthy--;
          queue.push([nr, nc]);
        }
      }
    }
  }
  return healthy === 0 ? minutes : -1;
}

assert.equal(minutesUntilAllFail([[1, 1, 0, 2], [0, 1, 1, 1], [0, 0, 0, 1]]), 5);
assert.equal(minutesUntilAllFail([[1, 1, 0, 2], [0, 1, 1, 1], [1, 0, 0, 1]]), -1);
assert.equal(minutesUntilAllFail([[0, 2]]), 0);
assert.equal(minutesUntilAllFail([[0]]), 0);
assert.equal(minutesUntilAllFail([[1]]), -1);
assert.equal(minutesUntilAllFail([[2, 1]]), 1);
assert.equal(minutesUntilAllFail([[2, 1, 1, 1, 2]]), 2);
console.log("131 ok");
