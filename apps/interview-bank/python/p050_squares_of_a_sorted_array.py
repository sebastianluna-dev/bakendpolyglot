# 050 · Squares of a Sorted Array


def sorted_squares(nums: list[int]) -> list[int]:
    out = [0] * len(nums)
    lo, hi = 0, len(nums) - 1
    for k in range(len(nums) - 1, -1, -1):
        # the largest square left is at one of the two ends
        if abs(nums[lo]) > abs(nums[hi]):
            out[k] = nums[lo] * nums[lo]
            lo += 1
        else:
            out[k] = nums[hi] * nums[hi]
            hi -= 1
    return out


if __name__ == "__main__":
    assert sorted_squares([-5, -2, 0, 3, 4]) == [0, 4, 9, 16, 25]
    assert sorted_squares([-3, -3, -1]) == [1, 9, 9]
    assert sorted_squares([]) == []
    assert sorted_squares([2, 5]) == [4, 25]
    assert sorted_squares([-7]) == [49]
    assert sorted_squares([-4, -1, 1, 4]) == [1, 1, 16, 16]
    print("050 ok")
