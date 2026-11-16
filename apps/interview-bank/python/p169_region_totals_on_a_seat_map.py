# 169 · Region Totals on a Seat Map


class RegionSums:
    def __init__(self, grid: list[list[int]]) -> None:
        rows, cols = len(grid), len(grid[0])
        # prefix[r][c] = total of the rows above r and the columns left of c
        self.prefix = [[0] * (cols + 1) for _ in range(rows + 1)]
        p = self.prefix
        for r in range(rows):
            for c in range(cols):
                p[r + 1][c + 1] = grid[r][c] + p[r][c + 1] + p[r + 1][c] - p[r][c]

    def sum(self, r1: int, c1: int, r2: int, c2: int) -> int:
        p = self.prefix
        return p[r2 + 1][c2 + 1] - p[r1][c2 + 1] - p[r2 + 1][c1] + p[r1][c1]


if __name__ == "__main__":
    seats = RegionSums([
        [3, 0, 1, 4],
        [5, 6, 3, 2],
        [1, 2, 0, 1],
        [4, 1, 0, 5],
    ])
    assert seats.sum(1, 1, 2, 2) == 11
    assert seats.sum(0, 0, 3, 3) == 38
    assert seats.sum(2, 3, 2, 3) == 1
    assert seats.sum(0, 1, 3, 2) == 13
    assert seats.sum(1, 0, 1, 3) == 16
    assert seats.sum(2, 2, 3, 3) == 6
    assert RegionSums([[7]]).sum(0, 0, 0, 0) == 7
    print("169 ok")
