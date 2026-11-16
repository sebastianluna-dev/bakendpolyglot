# 190 · Hand Out Candy Fairly


def min_candy(scores: list[int]) -> int:
    n = len(scores)
    candy = [1] * n
    for i in range(1, n):
        if scores[i] > scores[i - 1]:
            candy[i] = candy[i - 1] + 1
    for i in range(n - 2, -1, -1):
        # max keeps what the first pass gave for the left neighbour
        if scores[i] > scores[i + 1]:
            candy[i] = max(candy[i], candy[i + 1] + 1)
    return sum(candy)


if __name__ == "__main__":
    assert min_candy([1, 3, 2, 2, 1]) == 7
    assert min_candy([5, 4, 3, 4]) == 8
    assert min_candy([]) == 0
    assert min_candy([7]) == 1
    assert min_candy([2, 2, 2]) == 3
    assert min_candy([1, 2, 3, 4]) == 10
    assert min_candy([1, 2, 3, 4, 1]) == 11
    assert min_candy([4, 3, 2, 1]) == 10
    print("190 ok")
