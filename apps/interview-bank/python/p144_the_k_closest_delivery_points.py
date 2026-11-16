# 144 · The k Closest Delivery Points
import heapq


def k_closest(points: list[list[int]], k: int) -> list[list[int]]:
    heap: list[tuple[int, int, int]] = []  # negated (distance², x, y): farthest kept point on top
    for x, y in points:
        heapq.heappush(heap, (-(x * x + y * y), -x, -y))
        if len(heap) > k:
            heapq.heappop(heap)
    result: list[list[int]] = [[] for _ in range(len(heap))]
    for i in range(len(result) - 1, -1, -1):
        _, x, y = heapq.heappop(heap)
        result[i] = [-x, -y]
    return result


if __name__ == "__main__":
    assert k_closest([[3, 4], [1, -2], [-2, 1], [0, 5], [2, 2]], 3) == [[-2, 1], [1, -2], [2, 2]]
    assert k_closest([[2, 0], [0, 2], [0, -2], [-2, 0]], 2) == [[-2, 0], [0, -2]]
    assert k_closest([[5, 5], [1, 1]], 0) == []
    assert k_closest([[6, 1], [-1, -1], [0, 0]], 3) == [[0, 0], [-1, -1], [6, 1]]
    assert k_closest([[1, 1], [0, 3], [1, 1]], 2) == [[1, 1], [1, 1]]
    assert k_closest([[10000, -10000], [-10000, 10000]], 1) == [[-10000, 10000]]
    print("144 ok")
