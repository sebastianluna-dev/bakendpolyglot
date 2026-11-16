# 058 · Shortest Stretch Reaching a Total


def shortest_reaching(weights: list[int], target: int) -> int:
    n = len(weights)
    best = n + 1  # longer than any real stretch: means "none found"
    left = total = 0
    for right in range(n):
        total += weights[right]
        while total >= target:
            best = min(best, right - left + 1)
            total -= weights[left]
            left += 1
    return 0 if best == n + 1 else best


if __name__ == "__main__":
    assert shortest_reaching([3, 1, 4, 1, 5, 9, 2], 15) == 3
    assert shortest_reaching([2, 2, 2], 7) == 0
    assert shortest_reaching([], 5) == 0
    assert shortest_reaching([8], 8) == 1
    assert shortest_reaching([1, 1, 1, 1], 4) == 4
    assert shortest_reaching([1, 6, 1, 1, 7], 7) == 1
    print("058 ok")
