# 139 · Cheapest Route With at Most k Stops
import math


def cheapest_route(n: int, legs: list[list[int]], source: int, target: int, k: int) -> int:
    cost = [math.inf] * n
    cost[source] = 0
    for _ in range(k + 1):
        nxt = cost[:]  # relax from last round's costs only, so each round adds at most one leg
        for frm, to, price in legs:
            if cost[frm] != math.inf and cost[frm] + price < nxt[to]:
                nxt[to] = cost[frm] + price
        cost = nxt
    return -1 if cost[target] == math.inf else int(cost[target])


if __name__ == "__main__":
    legs = [[0, 1, 100], [1, 2, 100], [2, 3, 100], [0, 2, 500], [1, 3, 600]]
    triangle = [[0, 1, 1], [1, 2, 1], [0, 2, 5]]
    assert cheapest_route(4, legs, 0, 3, 1) == 600
    assert cheapest_route(4, legs, 0, 3, 2) == 300
    assert cheapest_route(4, legs, 0, 3, 0) == -1
    assert cheapest_route(4, legs, 0, 3, 10) == 300
    assert cheapest_route(3, triangle, 0, 2, 0) == 5
    assert cheapest_route(3, triangle, 0, 2, 1) == 2
    assert cheapest_route(2, [[0, 1, 7]], 1, 1, 0) == 0
    assert cheapest_route(3, [[0, 1, 4]], 0, 2, 5) == -1
    assert cheapest_route(2, [[1, 0, 3]], 0, 1, 3) == -1
    print("139 ok")
