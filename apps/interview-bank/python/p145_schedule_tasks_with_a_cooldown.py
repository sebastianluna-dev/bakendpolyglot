# 145 · Schedule Tasks With a Cooldown
import heapq
from collections import Counter, deque


def schedule_time(tasks: str, cooldown: int) -> int:
    heap = [-c for c in Counter(tasks).values()]  # negated copies left per type, most first
    heapq.heapify(heap)
    waiting: deque[tuple[int, int]] = deque()  # (copies left, time it may run again)
    time = 0
    while heap or waiting:
        time += 1
        if not heap:
            time = waiting[0][1]  # idle until the next type comes off cooldown
        else:
            left = -heapq.heappop(heap) - 1
            if left > 0:
                waiting.append((left, time + cooldown))
        if waiting and waiting[0][1] == time:
            heapq.heappush(heap, -waiting.popleft()[0])
    return time


if __name__ == "__main__":
    assert schedule_time("AAABBC", 2) == 7
    assert schedule_time("AABBCD", 1) == 6
    assert schedule_time("AAABBB", 2) == 8
    assert schedule_time("AAAA", 3) == 13
    assert schedule_time("ABAB", 0) == 4
    assert schedule_time("A", 5) == 1
    assert schedule_time("AAABCDEFG", 2) == 9
    print("145 ok")
