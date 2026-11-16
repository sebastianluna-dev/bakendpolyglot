# 064 · Rain Caught Between Walls


def trapped_rain(heights: list[int]) -> int:
    left, right = 0, len(heights) - 1
    left_max = right_max = water = 0
    while left < right:
        # The lower end decides: the far side has a wall at least this high.
        if heights[left] < heights[right]:
            left_max = max(left_max, heights[left])
            water += left_max - heights[left]
            left += 1
        else:
            right_max = max(right_max, heights[right])
            water += right_max - heights[right]
            right -= 1
    return water


if __name__ == "__main__":
    assert trapped_rain([3, 0, 2, 0, 4]) == 7
    assert trapped_rain([5, 4, 3]) == 0
    assert trapped_rain([]) == 0
    assert trapped_rain([7]) == 0
    assert trapped_rain([2, 0, 2]) == 2
    assert trapped_rain([0, 2, 0, 1, 0, 3, 1, 0, 2]) == 8
    assert trapped_rain([4, 1, 1, 4]) == 6
    assert trapped_rain([3, 0, 1]) == 1
    print("064 ok")
