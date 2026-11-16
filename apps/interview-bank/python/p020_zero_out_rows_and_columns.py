# 020 · Zero Out Rows and Columns


def zero_out(grid: list[list[int]]) -> None:
    zero_row = [False] * len(grid)
    zero_col = [False] * len(grid[0])
    for r, row in enumerate(grid):
        for c, value in enumerate(row):
            if value == 0:
                zero_row[r] = zero_col[c] = True
    for r, row in enumerate(grid):
        for c in range(len(row)):
            if zero_row[r] or zero_col[c]:
                row[c] = 0


def zeroed(grid: list[list[int]]) -> list[list[int]]:
    zero_out(grid)
    return grid


if __name__ == "__main__":
    assert zeroed([[1, 2, 3], [4, 0, 6], [7, 8, 9]]) == [[1, 0, 3], [0, 0, 0], [7, 0, 9]]
    assert zeroed([[0, 1, 2, 0], [3, 4, 5, 6], [1, 3, 1, 5]]) == [[0, 0, 0, 0], [0, 4, 5, 0], [0, 3, 1, 0]]
    assert zeroed([[0, 1], [1, 1]]) == [[0, 0], [0, 1]]
    assert zeroed([[1, 2], [3, 4]]) == [[1, 2], [3, 4]]
    assert zeroed([[0]]) == [[0]]
    assert zeroed([[5, 0, 7]]) == [[0, 0, 0]]
    print("020 ok")
