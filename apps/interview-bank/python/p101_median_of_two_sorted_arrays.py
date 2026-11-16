# 101 · Median of Two Sorted Arrays
import math


def median_of_two_sorted(a: list[int], b: list[int]) -> float:
    if len(a) > len(b):
        return median_of_two_sorted(b, a)
    m, n = len(a), len(b)
    half = (m + n + 1) // 2
    lo, hi = 0, m  # m stands for "take all of a"
    while lo < hi:
        mid = lo + (hi - lo) // 2  # values of a in the left half
        rest = half - mid
        if rest == 0 or b[rest - 1] <= a[mid]:
            hi = mid
        else:
            lo = mid + 1
    i = lo
    j = half - i
    left_max = max(a[i - 1] if i > 0 else -math.inf, b[j - 1] if j > 0 else -math.inf)
    if (m + n) % 2 == 1:
        return float(left_max)
    right_min = min(a[i] if i < m else math.inf, b[j] if j < n else math.inf)
    return (left_max + right_min) / 2


if __name__ == "__main__":
    assert median_of_two_sorted([12, 18, 31], [9, 20, 25, 40]) == 20
    assert median_of_two_sorted([5, 9], [6, 30]) == 7.5
    assert median_of_two_sorted([], [4]) == 4
    assert median_of_two_sorted([1, 2], []) == 1.5
    assert median_of_two_sorted([2, 2, 2], [2, 2]) == 2
    assert median_of_two_sorted([1, 2], [3, 4, 5, 6]) == 3.5
    assert median_of_two_sorted([1, 3, 5, 7, 9], [2]) == 4
    assert median_of_two_sorted([-5, -1], [-3]) == -3
    assert median_of_two_sorted([10], [1, 2, 3]) == 2.5
    print("101 ok")
