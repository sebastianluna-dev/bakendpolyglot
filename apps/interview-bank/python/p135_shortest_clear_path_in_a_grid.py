# 135 · Shortest Clear Path in a Grid
from collections import deque


def shortest_clear_path(grid: list[list[int]]) -> int:
    rows, cols = len(grid), len(grid[0])
    if grid[0][0] == 1 or grid[rows - 1][cols - 1] == 1:
        return -1
    cells = [[0] * cols for _ in range(rows)]  # 0 = not reached
    cells[0][0] = 1
    queue = deque([(0, 0)])
    while queue:
        r, c = queue.popleft()
        if r == rows - 1 and c == cols - 1:
            return cells[r][c]
        for dr in (-1, 0, 1):
            for dc in (-1, 0, 1):
                nr, nc = r + dr, c + dc
                if not (0 <= nr < rows and 0 <= nc < cols):
                    continue
                if grid[nr][nc] == 1 or cells[nr][nc] != 0:
                    continue  # also skips (r, c) itself
                cells[nr][nc] = cells[r][c] + 1
                queue.append((nr, nc))
    return -1


if __name__ == "__main__":
    assert shortest_clear_path([[0, 0, 0], [1, 1, 0], [1, 1, 0]]) == 4
    assert shortest_clear_path([[0, 1], [1, 0]]) == 2
    assert shortest_clear_path([[0]]) == 1
    assert shortest_clear_path([[1]]) == -1
    assert shortest_clear_path([[1, 0], [0, 0]]) == -1
    assert shortest_clear_path([[0, 0], [0, 1]]) == -1
    assert shortest_clear_path([[0, 1, 0], [1, 1, 0], [0, 0, 0]]) == -1
    assert shortest_clear_path([[0, 0, 0, 0]]) == 4
    assert shortest_clear_path([[0, 1, 0, 0], [0, 1, 0, 1], [0, 1, 0, 1], [1, 0, 1, 0]]) == 6
    print("135 ok")
