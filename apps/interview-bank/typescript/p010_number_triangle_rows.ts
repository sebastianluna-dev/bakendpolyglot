// 010 · Number Triangle Rows
import assert from "node:assert/strict";

export function triangleRows(n: number): number[][] {
  const rows: number[][] = [];
  for (let r = 0; r < n; r++) {
    const row = new Array<number>(r + 1).fill(1);
    for (let c = 1; c < r; c++) row[c] = rows[r - 1][c - 1] + rows[r - 1][c];
    rows.push(row);
  }
  return rows;
}

assert.deepEqual(triangleRows(5), [[1], [1, 1], [1, 2, 1], [1, 3, 3, 1], [1, 4, 6, 4, 1]]);
assert.deepEqual(triangleRows(2), [[1], [1, 1]]);
assert.deepEqual(triangleRows(1), [[1]]);
assert.deepEqual(triangleRows(0), []);
assert.equal(triangleRows(30)[29][14], 77558760);
console.log("010 ok");
