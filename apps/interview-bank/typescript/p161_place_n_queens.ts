// 161 · Place n Queens
import assert from "node:assert/strict";

export function placeQueens(n: number): string[][] {
  const result: string[][] = [];
  const queens: number[] = []; // queens[r] is the column of the queen in row r
  const cols = new Array<boolean>(n).fill(false);
  const downs = new Array<boolean>(2 * n - 1).fill(false); // r + c is the same along a down-left diagonal
  const ups = new Array<boolean>(2 * n - 1).fill(false); // r - c + n - 1 is the same along a down-right diagonal
  function walk(r: number): void {
    if (r === n) {
      result.push(queens.map((c) => ".".repeat(c) + "Q" + ".".repeat(n - c - 1)));
      return;
    }
    for (let c = 0; c < n; c++) {
      if (cols[c] || downs[r + c] || ups[r - c + n - 1]) continue;
      cols[c] = downs[r + c] = ups[r - c + n - 1] = true;
      queens.push(c);
      walk(r + 1);
      queens.pop();
      cols[c] = downs[r + c] = ups[r - c + n - 1] = false;
    }
  }
  walk(0);
  return result;
}

assert.deepEqual(placeQueens(4), [
  [".Q..", "...Q", "Q...", "..Q."],
  ["..Q.", "Q...", "...Q", ".Q.."],
]);
assert.deepEqual(placeQueens(1), [["Q"]]);
assert.deepEqual(placeQueens(2), []);
assert.deepEqual(placeQueens(3), []);
assert.equal(placeQueens(6).length, 4);
const eight = placeQueens(8);
assert.equal(eight.length, 92);
assert.deepEqual(eight[0], ["Q.......", "....Q...", ".......Q", ".....Q..", "..Q.....", "......Q.", ".Q......", "...Q...."]);
console.log("161 ok");
