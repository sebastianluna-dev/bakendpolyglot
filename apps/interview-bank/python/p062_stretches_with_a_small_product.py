# 062 · Stretches With a Small Product


def count_small_products(factors: list[int], limit: int) -> int:
    if limit <= 1:
        return 0  # every product is at least 1
    left = count = 0
    product = 1  # product of factors[left..right]
    for right, f in enumerate(factors):
        product *= f
        while product >= limit:
            product //= factors[left]
            left += 1
        count += right - left + 1  # every stretch that ends at right and starts at left or later
    return count


if __name__ == "__main__":
    assert count_small_products([4, 2, 6, 1, 3], 15) == 10
    assert count_small_products([1, 1, 1], 1) == 0
    assert count_small_products([1, 1, 1], 2) == 6
    assert count_small_products([], 10) == 0
    assert count_small_products([7], 0) == 0
    assert count_small_products([10, 5, 2], 100) == 5
    assert count_small_products([1000, 1000], 1000000) == 2
    print("062 ok")
