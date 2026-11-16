# 019 · Validate a Sudoku Board


def is_valid_sudoku(board: list[str]) -> bool:
    in_row = [[False] * 9 for _ in range(9)]  # not [[False] * 9] * 9: that is one row shared nine times
    in_col = [[False] * 9 for _ in range(9)]
    in_box = [[False] * 9 for _ in range(9)]
    for r in range(9):
        for c in range(9):
            ch = board[r][c]
            if ch == ".":
                continue
            d = int(ch) - 1
            b = r // 3 * 3 + c // 3
            if in_row[r][d] or in_col[c][d] or in_box[b][d]:
                return False
            in_row[r][d] = in_col[c][d] = in_box[b][d] = True
    return True


if __name__ == "__main__":
    board = [".9.5.3.46", "5.3.46...", "1........", "9.5.....2", "..1.6...5", "4..9....1", ".583...2.", "3.46.9758", ".2...8.1."]

    def change(r: int, row: str) -> list[str]:
        return board[:r] + [row] + board[r + 1 :]

    assert is_valid_sudoku(board)
    assert not is_valid_sudoku(change(2, "1.9......"))  # box only
    assert not is_valid_sudoku(change(0, "69.5.3.46"))  # row only
    assert not is_valid_sudoku(change(4, "3.1.6...5"))  # column only
    assert is_valid_sudoku(["........."] * 9)
    print("019 ok")
