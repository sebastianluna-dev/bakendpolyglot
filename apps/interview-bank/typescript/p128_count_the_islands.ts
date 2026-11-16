// 128 · Count the Islands
import assert from "node:assert/strict";

export function countIslands(grid: number[][]): number {
  const rows = grid.length;
  const cols = grid[0].length;
  const dirs = [[1, 0], [-1, 0], [0, 1], [0, -1]];
  const seen = Array.from({ length: rows }, () => new Array<boolean>(cols).fill(false));
  let islands = 0;
  for (let sr = 0; sr < rows; sr++) {
    for (let sc = 0; sc < cols; sc++) {
      if (grid[sr][sc] !== 1 || seen[sr][sc]) continue;
      islands++;
      seen[sr][sc] = true;
      const queue: [number, number][] = [[sr, sc]];
      let head = 0;
      while (head < queue.length) {
        const [r, c] = queue[head++];
        for (const [dr, dc] of dirs) {
          const nr = r + dr;
          const nc = c + dc;
          if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && grid[nr][nc] === 1 && !seen[nr][nc]) {
            seen[nr][nc] = true;
            queue.push([nr, nc]);
          }
        }
      }
    }
  }
  return islands;
}

assert.equal(countIslands([[1, 1, 0, 0, 1], [1, 0, 0, 1, 1], [0, 0, 1, 0, 0], [1, 0, 0, 0, 1]]), 5);
assert.equal(countIslands([[1, 0, 1], [0, 1, 0], [1, 0, 1]]), 5);
assert.equal(countIslands([[0, 0], [0, 0]]), 0);
assert.equal(countIslands([[1, 1], [1, 1]]), 1);
assert.equal(countIslands([[1, 1, 1], [0, 0, 1], [1, 1, 1]]), 1);
assert.equal(countIslands([[1]]), 1);
console.log("128 ok");
