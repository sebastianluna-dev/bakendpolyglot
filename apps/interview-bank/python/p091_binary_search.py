# 091 · Binary Search


def binary_search(ids: list[int], target: int) -> int:
    lo, hi = 0, len(ids)  # the first ID >= target is somewhere in [lo, hi)
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if ids[mid] >= target:
            hi = mid
        else:
            lo = mid + 1
    return lo if lo < len(ids) and ids[lo] == target else -1


if __name__ == "__main__":
    ids = [3, 8, 15, 21, 40, 57]
    assert binary_search(ids, 21) == 3
    assert binary_search(ids, 10) == -1
    assert binary_search([], 5) == -1
    assert binary_search([7], 7) == 0
    assert binary_search(ids, 3) == 0
    assert binary_search(ids, 57) == 5
    assert binary_search(ids, 1) == -1
    assert binary_search(ids, 99) == -1
    print("091 ok")
