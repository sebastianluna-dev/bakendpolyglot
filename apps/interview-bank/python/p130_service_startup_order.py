# 130 · Service Startup Order
import heapq


def startup_order(n: int, dependencies: list[list[int]]) -> list[int]:
    dependents: list[list[int]] = [[] for _ in range(n)]
    waiting = [0] * n  # dependencies not yet started
    for service, needs in dependencies:
        dependents[needs].append(service)
        waiting[service] += 1
    ready = [s for s in range(n) if waiting[s] == 0]
    heapq.heapify(ready)
    order: list[int] = []
    while ready:
        service = heapq.heappop(ready)
        order.append(service)
        for d in dependents[service]:
            waiting[d] -= 1
            if waiting[d] == 0:
                heapq.heappush(ready, d)
    return order if len(order) == n else []


if __name__ == "__main__":
    assert startup_order(5, [[0, 3], [2, 4], [1, 0]]) == [3, 0, 1, 4, 2]
    assert startup_order(3, [[0, 1], [1, 2], [2, 0]]) == []
    assert startup_order(3, []) == [0, 1, 2]
    assert startup_order(1, []) == [0]
    assert startup_order(2, [[1, 1]]) == []
    assert startup_order(3, [[2, 0], [2, 0], [1, 2]]) == [0, 2, 1]
    assert startup_order(4, [[1, 0], [2, 3], [3, 2]]) == []
    print("130 ok")
