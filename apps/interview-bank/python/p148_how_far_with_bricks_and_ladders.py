# 148 · How Far With Bricks and Ladders
import heapq


def furthest_platform(heights: list[int], bricks: int, ladders: int) -> int:
    on_ladders: list[int] = []  # the climbs ladders cover so far
    for i in range(len(heights) - 1):
        climb = heights[i + 1] - heights[i]
        if climb <= 0:
            continue
        heapq.heappush(on_ladders, climb)
        if len(on_ladders) > ladders:
            bricks -= heapq.heappop(on_ladders)  # the smallest climb goes to bricks
        if bricks < 0:
            return i
    return len(heights) - 1


if __name__ == "__main__":
    assert furthest_platform([3, 8, 6, 10, 11, 15, 7], 4, 1) == 3
    assert furthest_platform([10, 12, 13, 25, 26], 4, 1) == 4
    assert furthest_platform([7], 0, 0) == 0
    assert furthest_platform([9, 7, 7, 3], 0, 0) == 3
    assert furthest_platform([1, 2], 0, 0) == 0
    assert furthest_platform([1, 2, 3, 4], 0, 3) == 3
    assert furthest_platform([1, 6, 2, 9, 4, 5], 13, 0) == 5
    assert furthest_platform([1, 6, 2, 9, 4, 5], 12, 0) == 4
    print("148 ok")
