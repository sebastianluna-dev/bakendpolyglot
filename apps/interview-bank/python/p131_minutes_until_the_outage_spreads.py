# 131 · Minutes Until the Outage Spreads
from collections import deque


def minutes_until_all_fail(grid: list[list[int]]) -> int:
    rows, cols = len(grid), len(grid[0])
    queue: deque[tuple[int, int]] = deque()
    healthy = 0
    for r in range(rows):
        for c in range(cols):
            if grid[r][c] == 2:
                queue.append((r, c))
            elif grid[r][c] == 1:
                healthy += 1
    minutes = 0
    while queue and healthy > 0:
        minutes += 1
        for _ in range(len(queue)):  # one minute: only the servers that failed last minute
            r, c = queue.popleft()
            for nr, nc in ((r + 1, c), (r - 1, c), (r, c + 1), (r, c - 1)):
                if 0 <= nr < rows and 0 <= nc < cols and grid[nr][nc] == 1:
                    grid[nr][nc] = 2
                    healthy -= 1
                    queue.append((nr, nc))
    return minutes if healthy == 0 else -1


if __name__ == "__main__":
    assert minutes_until_all_fail([[1, 1, 0, 2], [0, 1, 1, 1], [0, 0, 0, 1]]) == 5
    assert minutes_until_all_fail([[1, 1, 0, 2], [0, 1, 1, 1], [1, 0, 0, 1]]) == -1
    assert minutes_until_all_fail([[0, 2]]) == 0
    assert minutes_until_all_fail([[0]]) == 0
    assert minutes_until_all_fail([[1]]) == -1
    assert minutes_until_all_fail([[2, 1]]) == 1
    assert minutes_until_all_fail([[2, 1, 1, 1, 2]]) == 2
    print("131 ok")
