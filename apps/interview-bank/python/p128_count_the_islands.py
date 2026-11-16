# 128 · Count the Islands
from collections import deque


def count_islands(grid: list[list[int]]) -> int:
    rows, cols = len(grid), len(grid[0])
    seen = [[False] * cols for _ in range(rows)]
    islands = 0
    for sr in range(rows):
        for sc in range(cols):
            if grid[sr][sc] != 1 or seen[sr][sc]:
                continue
            islands += 1
            seen[sr][sc] = True
            queue = deque([(sr, sc)])
            while queue:
                r, c = queue.popleft()
                for nr, nc in ((r + 1, c), (r - 1, c), (r, c + 1), (r, c - 1)):
                    if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == 1 and not seen[nr][nc]:
                        seen[nr][nc] = True
                        queue.append((nr, nc))
    return islands


if __name__ == "__main__":
    assert count_islands([[1, 1, 0, 0, 1], [1, 0, 0, 1, 1], [0, 0, 1, 0, 0], [1, 0, 0, 0, 1]]) == 5
    assert count_islands([[1, 0, 1], [0, 1, 0], [1, 0, 1]]) == 5
    assert count_islands([[0, 0], [0, 0]]) == 0
    assert count_islands([[1, 1], [1, 1]]) == 1
    assert count_islands([[1, 1, 1], [0, 0, 1], [1, 1, 1]]) == 1
    assert count_islands([[1]]) == 1
    print("128 ok")
