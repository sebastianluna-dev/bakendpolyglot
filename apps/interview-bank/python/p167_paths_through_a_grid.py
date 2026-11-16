# 167 · Paths Through a Grid


def grid_paths(rows: int, cols: int) -> int:
    row = [1] * cols  # the top row: one path to each cell
    for _ in range(1, rows):
        for c in range(1, cols):
            row[c] += row[c - 1]  # from above (old row[c]) plus from the left
    return row[cols - 1]


if __name__ == "__main__":
    assert grid_paths(3, 4) == 10
    assert grid_paths(2, 2) == 2
    assert grid_paths(1, 1) == 1
    assert grid_paths(1, 5) == 1
    assert grid_paths(5, 1) == 1
    assert grid_paths(3, 7) == 28
    assert grid_paths(10, 10) == 48620
    assert grid_paths(17, 17) == 601080390
    print("167 ok")
