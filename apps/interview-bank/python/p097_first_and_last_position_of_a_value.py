# 097 · First and Last Position of a Value


def first_and_last(days: list[int], day: int) -> list[int]:
    first = first_at_least(days, day)
    if first == len(days) or days[first] != day:
        return [-1, -1]
    return [first, first_at_least(days, day + 1) - 1]  # one before the first value >= day + 1


def first_at_least(a: list[int], target: int) -> int:
    lo, hi = 0, len(a)
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if a[mid] >= target:
            hi = mid
        else:
            lo = mid + 1
    return lo


if __name__ == "__main__":
    due = [3, 5, 5, 5, 8, 9]
    assert first_and_last(due, 5) == [1, 3]
    assert first_and_last([3, 5, 8], 6) == [-1, -1]
    assert first_and_last([], 1) == [-1, -1]
    assert first_and_last([7, 7, 7, 7], 7) == [0, 3]
    assert first_and_last([1], 1) == [0, 0]
    assert first_and_last([1, 2, 2], 2) == [1, 2]
    assert first_and_last(due, 1) == [-1, -1]
    assert first_and_last(due, 10) == [-1, -1]
    assert first_and_last(due, 3) == [0, 0]
    assert first_and_last(due, 9) == [5, 5]
    print("097 ok")
