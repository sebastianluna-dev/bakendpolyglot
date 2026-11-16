# 016 · Product of Everything Else


def product_of_others(nums: list[int]) -> list[int]:
    out = [1] * len(nums)
    left = 1
    for i, x in enumerate(nums):
        out[i] = left  # product of everything before i
        left *= x
    right = 1
    for i in range(len(nums) - 1, -1, -1):
        out[i] *= right
        right *= nums[i]
    return out


if __name__ == "__main__":
    assert product_of_others([2, 3, 4, 5]) == [60, 40, 30, 24]
    assert product_of_others([4, 0, 5]) == [0, 20, 0]
    assert product_of_others([3, 0, 2, 0]) == [0, 0, 0, 0]
    assert product_of_others([-1, 2, -3]) == [-6, 3, -2]
    assert product_of_others([7]) == [1]
    assert product_of_others([]) == []
    print("016 ok")
