# 142 · Smash the Heaviest Stones
import heapq


def last_stone(stones: list[int]) -> int:
    heap = [-s for s in stones]  # heapq is a min-heap, so store negated weights
    heapq.heapify(heap)
    while len(heap) > 1:
        x = -heapq.heappop(heap)
        y = -heapq.heappop(heap)
        if x > y:
            heapq.heappush(heap, -(x - y))
    return -heap[0] if heap else 0


if __name__ == "__main__":
    assert last_stone([3, 9, 4, 4, 1]) == 1
    assert last_stone([10, 4, 4, 2]) == 0
    assert last_stone([6, 6]) == 0
    assert last_stone([7]) == 7
    assert last_stone([1, 1, 1]) == 1
    assert last_stone([2, 2, 3]) == 1
    assert last_stone([5, 1, 1, 1, 1, 1]) == 0
    print("142 ok")
