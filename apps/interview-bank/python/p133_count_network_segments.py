# 133 · Count Network Segments


def count_segments(n: int, links: list[list[int]]) -> int:
    parent = list(range(n))
    size = [1] * n
    segments = n
    for a, b in links:
        ra, rb = find(parent, a), find(parent, b)
        if ra == rb:
            continue
        if size[ra] < size[rb]:
            ra, rb = rb, ra
        parent[rb] = ra  # the smaller tree goes under the larger one
        size[ra] += size[rb]
        segments -= 1
    return segments


def find(parent: list[int], x: int) -> int:
    while parent[x] != x:
        parent[x] = parent[parent[x]]  # path halving: point x at its grandparent
        x = parent[x]
    return x


if __name__ == "__main__":
    assert count_segments(5, [[0, 1], [1, 2], [3, 4]]) == 2
    assert count_segments(6, [[0, 1], [2, 3], [1, 0]]) == 4
    assert count_segments(1, []) == 1
    assert count_segments(3, []) == 3
    assert count_segments(3, [[0, 1], [1, 2], [2, 0]]) == 1
    assert count_segments(2, [[1, 1]]) == 2
    assert count_segments(6, [[0, 1], [2, 3], [4, 5], [1, 3], [5, 0]]) == 1
    print("133 ok")
