# 171 · Longest Rising Sequence


def longest_rising(nums: list[int]) -> int:
    tails: list[int] = []  # tails[k] = smallest last value of a rising sequence of length k + 1
    for x in nums:
        lo, hi = 0, len(tails)
        while lo < hi:
            # first tail >= x, so an equal value replaces instead of extending
            mid = (lo + hi) // 2
            if tails[mid] < x:
                lo = mid + 1
            else:
                hi = mid
        if lo == len(tails):
            tails.append(x)
        else:
            tails[lo] = x
    return len(tails)


if __name__ == "__main__":
    assert longest_rising([3, 1, 8, 2, 5, 9, 4]) == 4
    assert longest_rising([7, 7, 7]) == 1
    assert longest_rising([]) == 0
    assert longest_rising([5]) == 1
    assert longest_rising([9, 8, 7]) == 1
    assert longest_rising([1, 2, 3, 4]) == 4
    assert longest_rising([2, 2, 3, 3, 4]) == 3
    assert longest_rising([6, 2, 9, 3, 7, 4, 10, 5, 8]) == 5
    assert longest_rising([10, -3, 4, -1, 6, 2, 8]) == 4
    print("171 ok")
