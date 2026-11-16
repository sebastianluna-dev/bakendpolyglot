// 169 · Region Totals on a Seat Map
import assert from "node:assert/strict";

export class RegionSums {
  prefix: number[][]; // prefix[r][c] = total of the rows above r and the columns left of c

  constructor(grid: number[][]) {
    const rows = grid.length;
    const cols = grid[0].length;
    this.prefix = Array.from({ length: rows + 1 }, () => new Array<number>(cols + 1).fill(0));
    for (let r = 0; r < rows; r++) {
      for (let c = 0; c < cols; c++) {
        this.prefix[r + 1][c + 1] = grid[r][c] + this.prefix[r][c + 1] + this.prefix[r + 1][c] - this.prefix[r][c];
      }
    }
  }

  sum(r1: number, c1: number, r2: number, c2: number): number {
    const p = this.prefix;
    return p[r2 + 1][c2 + 1] - p[r1][c2 + 1] - p[r2 + 1][c1] + p[r1][c1];
  }
}

const seats = new RegionSums([
  [3, 0, 1, 4],
  [5, 6, 3, 2],
  [1, 2, 0, 1],
  [4, 1, 0, 5],
]);
assert.equal(seats.sum(1, 1, 2, 2), 11);
assert.equal(seats.sum(0, 0, 3, 3), 38);
assert.equal(seats.sum(2, 3, 2, 3), 1);
assert.equal(seats.sum(0, 1, 3, 2), 13);
assert.equal(seats.sum(1, 0, 1, 3), 16);
assert.equal(seats.sum(2, 2, 3, 3), 6);
assert.equal(new RegionSums([[7]]).sum(0, 0, 0, 0), 7);
console.log("169 ok");
