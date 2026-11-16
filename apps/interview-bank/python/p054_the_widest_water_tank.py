# 054 · The Widest Water Tank


def widest_tank(heights: list[int]) -> int:
    left, right, best = 0, len(heights) - 1, 0
    while left < right:
        area = (right - left) * min(heights[left], heights[right])
        best = max(best, area)
        # The shorter wall limits every tank that keeps it, so move it inward.
        if heights[left] <= heights[right]:
            left += 1
        else:
            right -= 1
    return best


if __name__ == "__main__":
    assert widest_tank([2, 6, 3, 1, 5, 2]) == 15
    assert widest_tank([4, 4]) == 4
    assert widest_tank([7]) == 0
    assert widest_tank([]) == 0
    assert widest_tank([1, 2, 3, 4, 5]) == 6
    assert widest_tank([0, 0, 0]) == 0
    assert widest_tank([5, 1, 1, 1, 5]) == 20
    print("054 ok")
