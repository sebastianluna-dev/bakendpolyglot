# 134 · Time for a Signal to Reach Every Node
import heapq
import math


def signal_time(n: int, links: list[list[int]], source: int) -> int:
    graph: list[list[tuple[int, int]]] = [[] for _ in range(n)]
    for frm, to, ms in links:
        graph[frm].append((to, ms))
    dist = [math.inf] * n
    dist[source] = 0
    heap = [(0, source)]  # (distance, node)
    while heap:
        d, node = heapq.heappop(heap)
        if d > dist[node]:
            continue  # stale: a shorter path was found after this was queued
        for nxt, ms in graph[node]:
            if d + ms < dist[nxt]:
                dist[nxt] = d + ms
                heapq.heappush(heap, (d + ms, nxt))
    slowest = max(dist)
    return -1 if slowest == math.inf else int(slowest)


if __name__ == "__main__":
    assert signal_time(4, [[0, 1, 4], [0, 2, 1], [2, 1, 2], [1, 3, 1], [2, 3, 6]], 0) == 4
    assert signal_time(3, [[0, 1, 2], [2, 1, 1]], 0) == -1
    assert signal_time(1, [], 0) == 0
    assert signal_time(2, [[0, 1, 5], [0, 1, 2]], 0) == 2
    assert signal_time(2, [[0, 1, 3]], 1) == -1
    assert signal_time(2, [[1, 0, 3]], 1) == 3
    assert signal_time(3, [[0, 1, 1], [1, 2, 1], [2, 0, 1]], 1) == 2
    assert signal_time(3, [[0, 1, 0], [1, 2, 0]], 0) == 0
    print("134 ok")
