# 176 · Largest Product of a Stretch


def largest_product(nums: list[int]) -> int:
    best = hi = lo = nums[0]  # hi, lo: largest and smallest product of a stretch ending here
    for x in nums[1:]:
        a, b = hi * x, lo * x
        hi = max(x, a, b)
        lo = min(x, a, b)
        best = max(best, hi)
    return best


if __name__ == "__main__":
    assert largest_product([2, -5, -2, 4, 0, 3]) == 80
    assert largest_product([-3, 0, -2]) == 0
    assert largest_product([-2]) == -2
    assert largest_product([-2, 3, -4]) == 24
    assert largest_product([0, 0]) == 0
    assert largest_product([-1, -1, -1]) == 1
    assert largest_product([3, -1, 4]) == 4
    assert largest_product([-4, 0, -5, 0, -6, 2]) == 2
    assert largest_product([10] * 9) == 1000000000
    print("176 ok")
