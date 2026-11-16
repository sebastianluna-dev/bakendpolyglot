# 123 · Is There a Route?
from collections import deque


def has_route(n: int, edges: list[list[int]], source: int, target: int) -> bool:
    graph: list[list[int]] = [[] for _ in range(n)]
    for a, b in edges:
        graph[a].append(b)
        graph[b].append(a)
    seen = [False] * n
    seen[source] = True
    queue = deque([source])
    while queue:
        node = queue.popleft()
        if node == target:
            return True
        for nxt in graph[node]:
            if not seen[nxt]:
                seen[nxt] = True  # mark when queued, so no node is queued twice
                queue.append(nxt)
    return False


if __name__ == "__main__":
    roads = [[0, 1], [1, 3], [3, 2], [4, 5]]
    assert has_route(6, roads, 0, 2)
    assert not has_route(6, roads, 0, 5)
    assert has_route(1, [], 0, 0)
    assert not has_route(3, [], 0, 2)
    assert has_route(2, [[1, 0]], 0, 1)
    assert has_route(4, [[0, 1], [1, 0], [1, 2], [2, 0], [2, 3]], 3, 0)
    print("123 ok")
