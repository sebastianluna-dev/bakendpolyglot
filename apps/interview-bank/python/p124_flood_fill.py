# 124 · Flood Fill
from collections import deque


def flood_fill(image: list[list[int]], row: int, col: int, color: int) -> list[list[int]]:
    old = image[row][col]
    if old == color:
        return image  # nothing to do, and the loop below would never stop
    rows, cols = len(image), len(image[0])
    image[row][col] = color
    queue = deque([(row, col)])
    while queue:
        r, c = queue.popleft()
        for nr, nc in ((r + 1, c), (r - 1, c), (r, c + 1), (r, c - 1)):
            if 0 <= nr < rows and 0 <= nc < cols and image[nr][nc] == old:
                image[nr][nc] = color
                queue.append((nr, nc))
    return image


if __name__ == "__main__":
    assert flood_fill([[1, 1, 0], [0, 1, 0], [1, 0, 1]], 0, 0, 3) == [[3, 3, 0], [0, 3, 0], [1, 0, 1]]
    assert flood_fill([[4, 4], [4, 4]], 1, 1, 4) == [[4, 4], [4, 4]]
    assert flood_fill([[0]], 0, 0, 7) == [[7]]
    assert flood_fill([[1, 2, 1], [1, 2, 1], [1, 1, 1]], 0, 2, 9) == [[9, 2, 9], [9, 2, 9], [9, 9, 9]]
    assert flood_fill([[5, 5, 6], [6, 5, 6]], 1, 2, 0) == [[5, 5, 0], [6, 5, 0]]
    print("124 ok")
