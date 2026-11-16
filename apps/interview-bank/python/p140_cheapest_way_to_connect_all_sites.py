# 140 · Cheapest Way to Connect All Sites


def cheapest_connection(n: int, cables: list[list[int]]) -> int:
    parent = list(range(n))
    size = [1] * n
    total = used = 0
    for a, b, cost in sorted(cables, key=lambda c: c[2]):
        ra, rb = find(parent, a), find(parent, b)
        if ra == rb:
            continue  # both ends already connected: this cable would close a loop
        if size[ra] < size[rb]:
            ra, rb = rb, ra
        parent[rb] = ra
        size[ra] += size[rb]
        total += cost
        used += 1
    return total if used == n - 1 else -1


def find(parent: list[int], x: int) -> int:
    while parent[x] != x:
        parent[x] = parent[parent[x]]  # path halving: point x at its grandparent
        x = parent[x]
    return x


if __name__ == "__main__":
    assert cheapest_connection(4, [[0, 1, 3], [1, 2, 1], [2, 3, 4], [0, 2, 2], [1, 3, 5]]) == 7
    assert cheapest_connection(3, [[0, 1, 5]]) == -1
    assert cheapest_connection(1, []) == 0
    assert cheapest_connection(2, []) == -1
    assert cheapest_connection(2, [[0, 1, 9], [1, 0, 4], [0, 0, 1]]) == 4
    assert cheapest_connection(5, [[0, 1, 2], [1, 2, 2], [2, 0, 2], [3, 4, 1], [2, 3, 7], [0, 4, 8]]) == 12
    print("140 ok")
