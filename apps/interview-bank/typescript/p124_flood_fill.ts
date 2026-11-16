// 124 · Flood Fill
import assert from "node:assert/strict";

export function floodFill(image: number[][], row: number, col: number, color: number): number[][] {
  const old = image[row][col];
  if (old === color) return image; // nothing to do, and the loop below would never stop
  const rows = image.length;
  const cols = image[0].length;
  const dirs = [[1, 0], [-1, 0], [0, 1], [0, -1]];
  image[row][col] = color;
  const queue: [number, number][] = [[row, col]];
  let head = 0;
  while (head < queue.length) {
    const [r, c] = queue[head++];
    for (const [dr, dc] of dirs) {
      const nr = r + dr;
      const nc = c + dc;
      if (nr >= 0 && nr < rows && nc >= 0 && nc < cols && image[nr][nc] === old) {
        image[nr][nc] = color;
        queue.push([nr, nc]);
      }
    }
  }
  return image;
}

assert.deepEqual(floodFill([[1, 1, 0], [0, 1, 0], [1, 0, 1]], 0, 0, 3), [[3, 3, 0], [0, 3, 0], [1, 0, 1]]);
assert.deepEqual(floodFill([[4, 4], [4, 4]], 1, 1, 4), [[4, 4], [4, 4]]);
assert.deepEqual(floodFill([[0]], 0, 0, 7), [[7]]);
assert.deepEqual(floodFill([[1, 2, 1], [1, 2, 1], [1, 1, 1]], 0, 2, 9), [[9, 2, 9], [9, 2, 9], [9, 9, 9]]);
assert.deepEqual(floodFill([[5, 5, 6], [6, 5, 6]], 1, 2, 0), [[5, 5, 0], [6, 5, 0]]);
console.log("124 ok");
