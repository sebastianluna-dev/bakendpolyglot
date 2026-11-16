# 098 · Find a Peak


def find_peak(altitudes: list[int]) -> int:
    lo, hi = 0, len(altitudes) - 1  # the last index can be the answer, but has no right neighbour to test
    while lo < hi:
        mid = lo + (hi - lo) // 2
        # Going down after mid: there is a peak at mid or to its left.
        if altitudes[mid] > altitudes[mid + 1]:
            hi = mid
        else:
            lo = mid + 1
    return lo


def is_peak(a: list[int], i: int) -> bool:
    return 0 <= i < len(a) and (i == 0 or a[i - 1] < a[i]) and (i == len(a) - 1 or a[i + 1] < a[i])


if __name__ == "__main__":
    routes = [
        [120, 135, 150, 140, 160, 155],
        [10, 20, 30],
        [5],
        [3, 1],
        [1, 3],
        [5, 4, 3, 2, 1],
        [1, 3, 2, 4, 3, 5, 4],
        [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 9],
    ]
    for route in routes:
        assert is_peak(route, find_peak(route)), route
    print("098 ok")
