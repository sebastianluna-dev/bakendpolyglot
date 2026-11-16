# 186 · Insert a New Interval


def insert_interval(intervals: list[list[int]], new_interval: list[int]) -> list[list[int]]:
    result: list[list[int]] = []
    start, end = new_interval
    i = 0
    while i < len(intervals) and intervals[i][1] < start:
        result.append(intervals[i])
        i += 1
    while i < len(intervals) and intervals[i][0] <= end:
        start = min(start, intervals[i][0])
        end = max(end, intervals[i][1])
        i += 1
    result.append([start, end])
    result.extend(intervals[i:])
    return result


if __name__ == "__main__":
    assert insert_interval([[1, 3], [6, 8], [10, 12], [15, 18]], [7, 10]) == [[1, 3], [6, 12], [15, 18]]
    assert insert_interval([[2, 4], [9, 11]], [5, 7]) == [[2, 4], [5, 7], [9, 11]]
    assert insert_interval([], [4, 6]) == [[4, 6]]
    assert insert_interval([[5, 6]], [1, 2]) == [[1, 2], [5, 6]]
    assert insert_interval([[1, 2]], [7, 9]) == [[1, 2], [7, 9]]
    assert insert_interval([[3, 4], [6, 7]], [1, 10]) == [[1, 10]]
    assert insert_interval([[1, 3]], [3, 5]) == [[1, 5]]
    assert insert_interval([[5, 8]], [2, 5]) == [[2, 8]]
    print("186 ok")
