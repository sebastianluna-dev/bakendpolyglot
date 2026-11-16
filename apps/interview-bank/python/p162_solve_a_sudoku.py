# 162 · Solve a Sudoku


def solve_sudoku(board: list[str]) -> list[str]:
    grid = [list(row) for row in board]
    in_row = [[False] * 10 for _ in range(9)]
    in_col = [[False] * 10 for _ in range(9)]
    in_box = [[False] * 10 for _ in range(9)]
    empty: list[tuple[int, int]] = []
    for r in range(9):
        for c in range(9):
            if grid[r][c] == ".":
                empty.append((r, c))
            else:
                d = int(grid[r][c])
                in_row[r][d] = in_col[c][d] = in_box[r // 3 * 3 + c // 3][d] = True

    def fill(k: int) -> bool:
        if k == len(empty):
            return True
        r, c = empty[k]
        b = r // 3 * 3 + c // 3
        for d in range(1, 10):
            if in_row[r][d] or in_col[c][d] or in_box[b][d]:
                continue
            in_row[r][d] = in_col[c][d] = in_box[b][d] = True
            grid[r][c] = str(d)
            if fill(k + 1):
                return True  # keep the digits: the board is solved
            in_row[r][d] = in_col[c][d] = in_box[b][d] = False
            grid[r][c] = "."
        return False

    return ["".join(row) for row in grid] if fill(0) else []


if __name__ == "__main__":
    puzzle = ["..59..386", ".86....29", "1....3...", ".51...8..", "8.4..7...", "...46...1", ".12....4.", "..87..5..", "647.....8"]
    solved = ["475921386", "386574129", "129683475", "751392864", "864157293", "293468751", "512839647", "938746512", "647215938"]
    assert solve_sudoku(puzzle) == solved
    stuck = ["12345678.", "........9"] + ["........."] * 7
    assert solve_sudoku(stuck) == []
    assert solve_sudoku(solved) == solved
    easy = [".1.52.3..", "2.57..1.6", "43...1...", "39.21..48", "......5.1", "15...7963", "76..9.48.", "5.8.7.21.", "..1...637"]
    easy_solved = ["619528374", "285743196", "437961852", "396215748", "874639521", "152487963", "763192485", "548376219", "921854637"]
    assert solve_sudoku(easy) == easy_solved
    print("162 ok")
