# 024 · The Smallest Missing Positive


def smallest_missing_positive(nums: list[int]) -> int:
    n = len(nums)
    for i in range(n):
        # keep swapping until slot i holds a value that has no slot or is already in its slot
        while 1 <= nums[i] <= n and nums[nums[i] - 1] != nums[i]:
            j = nums[i] - 1
            nums[i], nums[j] = nums[j], nums[i]
    for i in range(n):
        if nums[i] != i + 1:
            return i + 1
    return n + 1


if __name__ == "__main__":
    assert smallest_missing_positive([3, 1, -2, 2, 7]) == 4
    assert smallest_missing_positive([5, 2, 2, 9]) == 1
    assert smallest_missing_positive([]) == 1
    assert smallest_missing_positive([1, 2, 3]) == 4
    assert smallest_missing_positive([0, -1, -5]) == 1
    assert smallest_missing_positive([1, 1]) == 2
    assert smallest_missing_positive([2, 3, 4, 1]) == 5
    assert smallest_missing_positive([2147483647, 1]) == 2
    print("024 ok")
