// 025 · Most Points on One Line
import assert from "node:assert/strict";

export function maxPointsOnLine(points: number[][]): number {
  let best = 0;
  for (let i = 0; i < points.length; i++) {
    const directions = new Map<string, number>(); // reduced direction "dx,dy" -> points after i on it
    let same = 1; // point i and its copies
    let most = 0;
    for (let j = i + 1; j < points.length; j++) {
      let dx = points[j][0] - points[i][0];
      let dy = points[j][1] - points[i][1];
      if (dx === 0 && dy === 0) {
        same++;
        continue;
      }
      const g = gcd(Math.abs(dx), Math.abs(dy));
      dx /= g;
      dy /= g;
      // (1, -1) and (-1, 1) are the same line: make dx positive, or dy if dx is 0
      if (dx < 0 || (dx === 0 && dy < 0)) {
        dx = -dx;
        dy = -dy;
      }
      const key = `${dx},${dy}`;
      const count = (directions.get(key) ?? 0) + 1;
      directions.set(key, count);
      most = Math.max(most, count);
    }
    best = Math.max(best, same + most);
  }
  return best;
}

function gcd(a: number, b: number): number {
  while (b !== 0) [a, b] = [b, a % b];
  return a;
}

assert.equal(maxPointsOnLine([[1, 1], [2, 3], [3, 5], [4, 2], [0, -1]]), 4);
assert.equal(maxPointsOnLine([[3, 4], [3, 4], [3, -1], [0, 0]]), 3);
assert.equal(maxPointsOnLine([]), 0);
assert.equal(maxPointsOnLine([[5, 5]]), 1);
assert.equal(maxPointsOnLine([[1, 1], [1, 1], [1, 1]]), 3);
assert.equal(maxPointsOnLine([[0, 0], [1, 1], [2, 0]]), 2);
assert.equal(maxPointsOnLine([[-2, 7], [0, 7], [9, 7], [1, 1]]), 3);
assert.equal(maxPointsOnLine([[0, 0], [1, -1], [-1, 1], [2, -2]]), 4);
console.log("025 ok");
