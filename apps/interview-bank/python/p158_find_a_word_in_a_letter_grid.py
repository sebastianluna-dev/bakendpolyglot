# 158 · Find a Word in a Letter Grid


def find_word(grid: list[str], word: str) -> bool:
    rows, cols = len(grid), len(grid[0])
    used = [[False] * cols for _ in range(rows)]

    def walk(r: int, c: int, k: int) -> bool:
        if k == len(word):
            return True
        if r < 0 or r >= rows or c < 0 or c >= cols or used[r][c] or grid[r][c] != word[k]:
            return False
        used[r][c] = True
        found = walk(r + 1, c, k + 1) or walk(r - 1, c, k + 1) or walk(r, c + 1, k + 1) or walk(r, c - 1, k + 1)
        used[r][c] = False  # free the cell for paths that start elsewhere
        return found

    return any(walk(r, c, 0) for r in range(rows) for c in range(cols))


if __name__ == "__main__":
    grid = ["she", "afl", "nao"]
    assert find_word(grid, "shelf")
    assert not find_word(grid, "lol")
    assert find_word(grid, "loan")
    assert find_word(grid, "fan")
    assert not find_word(grid, "sf")
    assert find_word(grid, "o")
    assert not find_word(grid, "z")
    assert not find_word(grid, "shelfaloans")
    assert find_word(["aa", "ab"], "aaab")
    print("158 ok")
