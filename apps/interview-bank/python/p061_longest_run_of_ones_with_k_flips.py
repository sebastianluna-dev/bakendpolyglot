# 061 · Longest Run of Ones With k Flips


def longest_ones(seats: list[int], k: int) -> int:
    left = zeros = best = 0  # zeros inside seats[left..right]
    for right, seat in enumerate(seats):
        if seat == 0:
            zeros += 1
        while zeros > k:
            if seats[left] == 0:
                zeros -= 1
            left += 1
        best = max(best, right - left + 1)
    return best


if __name__ == "__main__":
    assert longest_ones([1, 1, 0, 1, 0, 0, 1, 1, 1, 0, 1], 2) == 6
    assert longest_ones([1, 0, 1, 1, 1, 0], 0) == 3
    assert longest_ones([], 3) == 0
    assert longest_ones([0, 0, 0], 0) == 0
    assert longest_ones([0, 0, 0], 5) == 3
    assert longest_ones([1, 0, 0, 1], 1) == 2
    assert longest_ones([1, 1, 1], 1) == 3
    print("061 ok")
