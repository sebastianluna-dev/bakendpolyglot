# 099 · Search a Sorted Matrix


def search_sorted_matrix(shelves: list[list[int]], target: int) -> bool:
    rows = len(shelves)
    cols = len(shelves[0]) if rows else 0
    total = rows * cols
    lo, hi = 0, total  # positions 0 .. total - 1, read row by row as one sorted array
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if shelves[mid // cols][mid % cols] >= target:
            hi = mid
        else:
            lo = mid + 1
    return lo < total and shelves[lo // cols][lo % cols] == target


if __name__ == "__main__":
    branch = [
        [1, 4, 7, 11],
        [15, 18, 21, 26],
        [30, 34, 40, 52],
    ]
    column = [[1], [3], [5]]
    assert search_sorted_matrix(branch, 21) is True
    assert search_sorted_matrix(branch, 13) is False
    assert search_sorted_matrix([], 1) is False
    assert search_sorted_matrix([[]], 1) is False
    assert search_sorted_matrix([[5]], 5) is True
    assert search_sorted_matrix([[5]], 6) is False
    assert search_sorted_matrix(branch, 60) is False
    assert search_sorted_matrix(branch, 0) is False
    assert search_sorted_matrix(branch, 1) is True
    assert search_sorted_matrix(branch, 52) is True
    assert search_sorted_matrix(branch, 26) is True
    assert search_sorted_matrix(branch, 30) is True
    assert search_sorted_matrix(column, 3) is True
    assert search_sorted_matrix(column, 4) is False
    print("099 ok")
