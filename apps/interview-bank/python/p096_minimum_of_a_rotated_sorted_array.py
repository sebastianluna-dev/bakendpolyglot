# 096 · Minimum of a Rotated Sorted Array


def rotated_minimum(times: list[int]) -> int:
    last = times[-1]
    lo, hi = 0, len(times)
    while lo < hi:
        mid = lo + (hi - lo) // 2
        # Values <= last belong to the run after the wrap, which starts at the minimum.
        if times[mid] <= last:
            hi = mid
        else:
            lo = mid + 1
    return times[lo]


if __name__ == "__main__":
    assert rotated_minimum([900, 1020, 1200, 60, 300, 480]) == 60
    assert rotated_minimum([60, 300, 480]) == 60
    assert rotated_minimum([7]) == 7
    assert rotated_minimum([2, 1]) == 1
    assert rotated_minimum([3, 4, 5, 1, 2]) == 1
    assert rotated_minimum([2, 3, 4, 5, 1]) == 1
    assert rotated_minimum([5, 1, 2, 3, 4]) == 1
    print("096 ok")
