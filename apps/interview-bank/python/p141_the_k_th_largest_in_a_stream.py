# 141 · The k-th Largest in a Stream
import heapq


class KthLargest:
    def __init__(self, k: int, nums: list[int]) -> None:
        self.k = k
        self.heap: list[int] = []  # the k largest values so far; the smallest is at index 0
        for x in nums:
            self.add(x)

    def add(self, value: int) -> int:
        heapq.heappush(self.heap, value)
        if len(self.heap) > self.k:
            heapq.heappop(self.heap)  # drop the smallest: it is no longer in the top k
        return self.heap[0]


if __name__ == "__main__":
    board = KthLargest(3, [40, 75, 20, 90])
    assert board.add(50) == 50
    assert board.add(10) == 50
    assert board.add(80) == 75
    assert board.add(95) == 80

    single = KthLargest(1, [])
    assert single.add(-3) == -3
    assert single.add(-5) == -3
    assert single.add(2) == 2

    ties = KthLargest(2, [7])
    assert ties.add(7) == 7
    assert ties.add(3) == 7
    assert ties.add(9) == 7
    assert ties.add(8) == 8

    many = KthLargest(4, [5, 1, 9, 3, 7, 2])
    assert many.add(4) == 4
    assert many.add(6) == 5
    assert many.add(10) == 6
    print("141 ok")
