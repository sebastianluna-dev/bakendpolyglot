# 185 · Merge Overlapping Intervals


def merge_intervals(intervals: list[list[int]]) -> list[list[int]]:
    merged: list[list[int]] = []
    for start, end in sorted(intervals, key=lambda iv: iv[0]):
        if merged and start <= merged[-1][1]:
            merged[-1][1] = max(merged[-1][1], end)
        else:
            merged.append([start, end])  # a new pair, so extending it never changes the input
    return merged


if __name__ == "__main__":
    assert merge_intervals([[9, 12], [1, 3], [2, 6], [8, 9]]) == [[1, 6], [8, 12]]
    assert merge_intervals([[1, 10], [2, 3], [4, 5]]) == [[1, 10]]
    assert merge_intervals([]) == []
    assert merge_intervals([[5, 5]]) == [[5, 5]]
    assert merge_intervals([[3, 4], [1, 2]]) == [[1, 2], [3, 4]]
    assert merge_intervals([[4, 6], [1, 4], [6, 6]]) == [[1, 6]]
    print("185 ok")
