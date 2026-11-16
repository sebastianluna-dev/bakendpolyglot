# 132 · Water That Reaches Both Coasts
from collections import deque


def reach(heights: list[list[int]], starts: list[tuple[int, int]]) -> list[list[bool]]:
    rows, cols = len(heights), len(heights[0])
    seen = [[False] * cols for _ in range(rows)]
    for r, c in starts:
        seen[r][c] = True
    queue = deque(starts)
    while queue:
        r, c = queue.popleft()
        for nr, nc in ((r + 1, c), (r - 1, c), (r, c + 1), (r, c - 1)):
            # uphill or level: water from (nr, nc) could flow down to (r, c)
            if 0 <= nr < rows and 0 <= nc < cols and not seen[nr][nc] and heights[nr][nc] >= heights[r][c]:
                seen[nr][nc] = True
                queue.append((nr, nc))
    return seen


def both_coasts(heights: list[list[int]]) -> list[tuple[int, int]]:
    rows, cols = len(heights), len(heights[0])
    north_west_edge = [(r, c) for r in range(rows) for c in range(cols) if r == 0 or c == 0]
    south_east_edge = [(r, c) for r in range(rows) for c in range(cols) if r == rows - 1 or c == cols - 1]
    north_west = reach(heights, north_west_edge)
    south_east = reach(heights, south_east_edge)
    return [(r, c) for r in range(rows) for c in range(cols) if north_west[r][c] and south_east[r][c]]


if __name__ == "__main__":
    assert both_coasts([[1, 2, 2, 3], [3, 2, 3, 4], [2, 4, 5, 3], [6, 7, 1, 4]]) == [(0, 3), (1, 3), (2, 2), (3, 0), (3, 1)]
    assert both_coasts([[5, 5, 5], [5, 1, 5], [5, 5, 5]]) == [(0, 0), (0, 1), (0, 2), (1, 0), (1, 2), (2, 0), (2, 1), (2, 2)]
    assert both_coasts([[4, 3, 2], [5, 1, 3], [6, 2, 1]]) == [(0, 0), (0, 1), (0, 2), (1, 0), (1, 2), (2, 0)]
    assert both_coasts([[1, 2, 3]]) == [(0, 0), (0, 1), (0, 2)]
    assert both_coasts([[7]]) == [(0, 0)]
    print("132 ok")
