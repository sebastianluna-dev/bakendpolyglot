# 182 · Hand Out Cookies


def content_children(greed: list[int], sizes: list[int]) -> int:
    greed.sort()
    sizes.sort()
    child = 0  # the least greedy child still without a cookie
    for size in sizes:
        if child < len(greed) and size >= greed[child]:
            child += 1
    return child


if __name__ == "__main__":
    assert content_children([5, 2, 8, 2], [3, 1, 6, 9]) == 3
    assert content_children([1, 3], [3, 1]) == 2
    assert content_children([5, 2, 8, 2], [3, 1, 6]) == 2
    assert content_children([4, 4], []) == 0
    assert content_children([], [1, 2]) == 0
    assert content_children([10, 20], [1, 2, 3]) == 0
    assert content_children([2, 2, 2], [2, 2]) == 2
    assert content_children([1, 2, 3], [3]) == 1
    print("182 ok")
