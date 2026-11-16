# 071 · Round-Robin Elimination
from collections import deque


def elimination_order(n: int, k: int) -> list[int]:
    circle = deque(range(1, n + 1))
    order: list[int] = []
    while circle:
        for _ in range((k - 1) % len(circle)):  # skip whole laps when k is larger than the circle
            circle.append(circle.popleft())
        order.append(circle.popleft())
    return order


if __name__ == "__main__":
    assert elimination_order(5, 2) == [2, 4, 1, 5, 3]
    assert elimination_order(4, 3) == [3, 2, 4, 1]
    assert elimination_order(1, 7) == [1]
    assert elimination_order(4, 1) == [1, 2, 3, 4]
    assert elimination_order(3, 5) == [2, 3, 1]
    assert elimination_order(6, 6) == [6, 1, 3, 2, 5, 4]
    print("071 ok")
