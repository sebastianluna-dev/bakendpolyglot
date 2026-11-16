# 011 · The Balance Point


def balance_point(nums: list[int]) -> int:
    total = sum(nums)
    left = 0
    for i, x in enumerate(nums):
        if left == total - left - x:
            return i
        left += x
    return -1


if __name__ == "__main__":
    assert balance_point([3, 1, 5, 2, 2]) == 2
    assert balance_point([1, 2, 3]) == -1
    assert balance_point([]) == -1
    assert balance_point([7]) == 0
    assert balance_point([5, 2, -2]) == 0
    assert balance_point([0, 0, 0]) == 0
    print("011 ok")
