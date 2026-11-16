# 095 · Search a Rotated Sorted Array


def search_rotated(times: list[int], target: int) -> int:
    n = len(times)
    if n == 0:
        return -1
    last = times[-1]
    lo, hi = 0, n
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if times[mid] <= last:
            hi = mid
        else:
            lo = mid + 1
    start = lo  # index of the minimum: [start, n) and [0, start) are both sorted
    lo, hi = (start, n) if target <= last else (0, start)
    i = first_at_least(times, lo, hi, target)
    return i if i < hi and times[i] == target else -1


def first_at_least(a: list[int], lo: int, hi: int, target: int) -> int:
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if a[mid] >= target:
            hi = mid
        else:
            lo = mid + 1
    return lo


if __name__ == "__main__":
    board = [900, 1020, 1200, 60, 300, 480]
    assert search_rotated(board, 300) == 4
    assert search_rotated(board, 600) == -1
    assert search_rotated(board, 1020) == 1
    assert search_rotated(board, 900) == 0
    assert search_rotated(board, 480) == 5
    assert search_rotated(board, 60) == 3
    assert search_rotated(board, 1300) == -1
    assert search_rotated(board, 10) == -1
    assert search_rotated([1, 2, 3], 3) == 2
    assert search_rotated([5], 5) == 0
    assert search_rotated([5], 1) == -1
    assert search_rotated([], 1) == -1
    assert search_rotated([2, 1], 1) == 1
    assert search_rotated([2, 1], 2) == 0
    print("095 ok")
