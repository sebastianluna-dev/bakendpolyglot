// 158 · Find a Word in a Letter Grid
import assert from "node:assert/strict";

export function findWord(grid: string[], word: string): boolean {
  const rows = grid.length;
  const cols = grid[0].length;
  const used = Array.from({ length: rows }, () => new Array<boolean>(cols).fill(false));
  function walk(r: number, c: number, k: number): boolean {
    if (k === word.length) return true;
    if (r < 0 || r >= rows || c < 0 || c >= cols || used[r][c] || grid[r][c] !== word[k]) return false;
    used[r][c] = true;
    const found = walk(r + 1, c, k + 1) || walk(r - 1, c, k + 1) || walk(r, c + 1, k + 1) || walk(r, c - 1, k + 1);
    used[r][c] = false; // free the cell for paths that start elsewhere
    return found;
  }
  for (let r = 0; r < rows; r++) {
    for (let c = 0; c < cols; c++) {
      if (walk(r, c, 0)) return true;
    }
  }
  return false;
}

const grid = ["she", "afl", "nao"];
assert.equal(findWord(grid, "shelf"), true);
assert.equal(findWord(grid, "lol"), false);
assert.equal(findWord(grid, "loan"), true);
assert.equal(findWord(grid, "fan"), true);
assert.equal(findWord(grid, "sf"), false);
assert.equal(findWord(grid, "o"), true);
assert.equal(findWord(grid, "z"), false);
assert.equal(findWord(grid, "shelfaloans"), false);
assert.equal(findWord(["aa", "ab"], "aaab"), true);
console.log("158 ok");
