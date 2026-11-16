# 065 · Maximum of Every Window
from collections import deque


def window_maxima(readings: list[int], k: int) -> list[int]:
    result: list[int] = []
    window: deque[int] = deque()  # indices; their readings decrease from window[0] to the back
    for i, r in enumerate(readings):
        while window and readings[window[-1]] <= r:
            window.pop()
        window.append(i)
        if window[0] <= i - k:
            window.popleft()
        if i >= k - 1:
            result.append(readings[window[0]])
    return result


if __name__ == "__main__":
    assert window_maxima([3, 1, -2, 5, 4, 2, 6], 3) == [3, 5, 5, 5, 6]
    assert window_maxima([2, 7, 1], 3) == [7]
    assert window_maxima([4, -1, 4], 1) == [4, -1, 4]
    assert window_maxima([], 2) == []
    assert window_maxima([1, 2], 3) == []
    assert window_maxima([5, 5, 1, 5], 2) == [5, 5, 5]
    assert window_maxima([9, 7, 5, 3], 2) == [9, 7, 5]
    assert window_maxima([9, 1, 1, 1], 2) == [9, 1, 1]
    print("065 ok")
