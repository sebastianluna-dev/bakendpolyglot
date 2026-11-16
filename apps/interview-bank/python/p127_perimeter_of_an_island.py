# 127 · Perimeter of an Island


def island_perimeter(grid: list[list[int]]) -> int:
    perimeter = 0
    for r, row in enumerate(grid):
        for c, cell in enumerate(row):
            if cell == 1:
                perimeter += 4
                if r > 0 and grid[r - 1][c] == 1:
                    perimeter -= 2  # the shared side is lost by both cells
                if c > 0 and row[c - 1] == 1:
                    perimeter -= 2
    return perimeter


if __name__ == "__main__":
    assert island_perimeter([[0, 1, 0], [1, 1, 1], [0, 1, 0]]) == 12
    assert island_perimeter([[1, 1], [1, 1]]) == 8
    assert island_perimeter([[1]]) == 4
    assert island_perimeter([[0, 0, 0], [0, 1, 0]]) == 4
    assert island_perimeter([[1, 1, 1, 0]]) == 8
    assert island_perimeter([[1, 1, 1], [1, 0, 1], [1, 1, 1]]) == 16
    print("127 ok")
