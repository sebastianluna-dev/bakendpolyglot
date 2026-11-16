# 025 · Most Points on One Line
from math import gcd


def max_points_on_line(points: list[list[int]]) -> int:
    best = 0
    for i in range(len(points)):
        directions: dict[tuple[int, int], int] = {}  # reduced direction -> points after i on it
        same = 1  # point i and its copies
        most = 0
        for j in range(i + 1, len(points)):
            dx = points[j][0] - points[i][0]
            dy = points[j][1] - points[i][1]
            if dx == 0 and dy == 0:
                same += 1
                continue
            g = gcd(dx, dy)
            dx //= g
            dy //= g
            # (1, -1) and (-1, 1) are the same line: make dx positive, or dy if dx is 0
            if dx < 0 or (dx == 0 and dy < 0):
                dx, dy = -dx, -dy
            count = directions.get((dx, dy), 0) + 1
            directions[(dx, dy)] = count
            most = max(most, count)
        best = max(best, same + most)
    return best


if __name__ == "__main__":
    assert max_points_on_line([[1, 1], [2, 3], [3, 5], [4, 2], [0, -1]]) == 4
    assert max_points_on_line([[3, 4], [3, 4], [3, -1], [0, 0]]) == 3
    assert max_points_on_line([]) == 0
    assert max_points_on_line([[5, 5]]) == 1
    assert max_points_on_line([[1, 1], [1, 1], [1, 1]]) == 3
    assert max_points_on_line([[0, 0], [1, 1], [2, 0]]) == 2
    assert max_points_on_line([[-2, 7], [0, 7], [9, 7], [1, 1]]) == 3
    assert max_points_on_line([[0, 0], [1, -1], [-1, 1], [2, -2]]) == 4
    print("025 ok")
