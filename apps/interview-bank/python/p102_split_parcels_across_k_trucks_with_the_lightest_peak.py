# 102 · Split Parcels Across k Trucks With the Lightest Peak


def lightest_peak(weights: list[int], k: int) -> int:
    lo, hi = max(weights), sum(weights)
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if trucks_needed(weights, mid) <= k:
            hi = mid
        else:
            lo = mid + 1
    return lo


def trucks_needed(weights: list[int], limit: int) -> int:
    trucks, load = 1, 0
    for w in weights:
        if load + w > limit:
            trucks += 1
            load = 0
        load += w
    return trucks


if __name__ == "__main__":
    assert lightest_peak([4, 9, 3, 6, 8, 2], 3) == 13
    assert lightest_peak([3, 1, 12, 2], 3) == 12
    assert lightest_peak([4, 9, 3], 1) == 16
    assert lightest_peak([2, 8, 5], 3) == 8
    assert lightest_peak([7], 1) == 7
    assert lightest_peak([1, 1, 1, 1, 1, 1], 4) == 2
    print("102 ok")
