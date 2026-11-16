// 132 · Water That Reaches Both Coasts
import assert from "node:assert/strict";

function reach(heights: number[][], starts: [number, number][]): boolean[][] {
  const rows = heights.length;
  const cols = heights[0].length;
  const dirs = [[1, 0], [-1, 0], [0, 1], [0, -1]];
  const seen = Array.from({ length: rows }, () => new Array<boolean>(cols).fill(false));
  for (const [r, c] of starts) seen[r][c] = true;
  const queue: [number, number][] = [...starts];
  let head = 0;
  while (head < queue.length) {
    const [r, c] = queue[head++];
    for (const [dr, dc] of dirs) {
      const nr = r + dr;
      const nc = c + dc;
      // uphill or level: water from (nr, nc) could flow down to (r, c)
      if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && !seen[nr][nc] && heights[nr][nc] >= heights[r][c]) {
        seen[nr][nc] = true;
        queue.push([nr, nc]);
      }
    }
  }
  return seen;
}

export function bothCoasts(heights: number[][]): number[][] {
  const rows = heights.length;
  const cols = heights[0].length;
  const northWestEdge: [number, number][] = [];
  const southEastEdge: [number, number][] = [];
  for (let r = 0; r < rows; r++) {
    for (let c = 0; c < cols; c++) {
      if (r === 0 || c === 0) northWestEdge.push([r, c]);
      if (r === rows - 1 || c === cols - 1) southEastEdge.push([r, c]);
    }
  }
  const northWest = reach(heights, northWestEdge);
  const southEast = reach(heights, southEastEdge);
  const cells: number[][] = [];
  for (let r = 0; r < rows; r++) {
    for (let c = 0; c < cols; c++) {
      if (northWest[r][c] && southEast[r][c]) cells.push([r, c]);
    }
  }
  return cells;
}

assert.deepEqual(bothCoasts([[1, 2, 2, 3], [3, 2, 3, 4], [2, 4, 5, 3], [6, 7, 1, 4]]), [[0, 3], [1, 3], [2, 2], [3, 0], [3, 1]]);
assert.deepEqual(bothCoasts([[5, 5, 5], [5, 1, 5], [5, 5, 5]]), [[0, 0], [0, 1], [0, 2], [1, 0], [1, 2], [2, 0], [2, 1], [2, 2]]);
assert.deepEqual(bothCoasts([[4, 3, 2], [5, 1, 3], [6, 2, 1]]), [[0, 0], [0, 1], [0, 2], [1, 0], [1, 2], [2, 0]]);
assert.deepEqual(bothCoasts([[1, 2, 3]]), [[0, 0], [0, 1], [0, 2]]);
assert.deepEqual(bothCoasts([[7]]), [[0, 0]]);
console.log("132 ok");
