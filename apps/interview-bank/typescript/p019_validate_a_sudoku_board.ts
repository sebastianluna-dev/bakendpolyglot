// 019 · Validate a Sudoku Board
import assert from "node:assert/strict";

export function isValidSudoku(board: string[]): boolean {
  const grid = () => Array.from({ length: 9 }, () => new Array<boolean>(9).fill(false));
  const inRow = grid();
  const inCol = grid();
  const inBox = grid();
  for (let r = 0; r < 9; r++) {
    for (let c = 0; c < 9; c++) {
      const ch = board[r][c];
      if (ch === ".") continue;
      const d = Number(ch) - 1;
      const b = Math.floor(r / 3) * 3 + Math.floor(c / 3);
      if (inRow[r][d] || inCol[c][d] || inBox[b][d]) return false;
      inRow[r][d] = inCol[c][d] = inBox[b][d] = true;
    }
  }
  return true;
}

const board = [".9.5.3.46", "5.3.46...", "1........", "9.5.....2", "..1.6...5", "4..9....1", ".583...2.", "3.46.9758", ".2...8.1."];
const change = (r: number, row: string) => board.map((old, i) => (i === r ? row : old));
assert.equal(isValidSudoku(board), true);
assert.equal(isValidSudoku(change(2, "1.9......")), false); // box only
assert.equal(isValidSudoku(change(0, "69.5.3.46")), false); // row only
assert.equal(isValidSudoku(change(4, "3.1.6...5")), false); // column only
assert.equal(isValidSudoku(new Array<string>(9).fill(".........")), true);
console.log("019 ok");
