// 162 · Solve a Sudoku
import assert from "node:assert/strict";

export function solveSudoku(board: string[]): string[] {
  const grid = board.map((row) => [...row]);
  const inRow = Array.from({ length: 9 }, () => new Array<boolean>(10).fill(false));
  const inCol = Array.from({ length: 9 }, () => new Array<boolean>(10).fill(false));
  const inBox = Array.from({ length: 9 }, () => new Array<boolean>(10).fill(false));
  const empty: [number, number][] = [];
  for (let r = 0; r < 9; r++) {
    for (let c = 0; c < 9; c++) {
      if (grid[r][c] === ".") {
        empty.push([r, c]);
      } else {
        const d = Number(grid[r][c]);
        inRow[r][d] = inCol[c][d] = inBox[Math.floor(r / 3) * 3 + Math.floor(c / 3)][d] = true;
      }
    }
  }
  function fill(k: number): boolean {
    if (k === empty.length) return true;
    const [r, c] = empty[k];
    const b = Math.floor(r / 3) * 3 + Math.floor(c / 3);
    for (let d = 1; d <= 9; d++) {
      if (inRow[r][d] || inCol[c][d] || inBox[b][d]) continue;
      inRow[r][d] = inCol[c][d] = inBox[b][d] = true;
      grid[r][c] = String(d);
      if (fill(k + 1)) return true; // keep the digits: the board is solved
      inRow[r][d] = inCol[c][d] = inBox[b][d] = false;
      grid[r][c] = ".";
    }
    return false;
  }
  return fill(0) ? grid.map((row) => row.join("")) : [];
}

const puzzle = ["..59..386", ".86....29", "1....3...", ".51...8..", "8.4..7...", "...46...1", ".12....4.", "..87..5..", "647.....8"];
const solved = ["475921386", "386574129", "129683475", "751392864", "864157293", "293468751", "512839647", "938746512", "647215938"];
assert.deepEqual(solveSudoku(puzzle), solved);
const stuck = ["12345678.", "........9", ...new Array<string>(7).fill(".........")];
assert.deepEqual(solveSudoku(stuck), []);
assert.deepEqual(solveSudoku(solved), solved);
const easy = [".1.52.3..", "2.57..1.6", "43...1...", "39.21..48", "......5.1", "15...7963", "76..9.48.", "5.8.7.21.", "..1...637"];
const easySolved = ["619528374", "285743196", "437961852", "396215748", "874639521", "152487963", "763192485", "548376219", "921854637"];
assert.deepEqual(solveSudoku(easy), easySolved);
console.log("162 ok");
