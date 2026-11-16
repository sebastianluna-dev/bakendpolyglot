# 094 · Integer Square Root


def integer_sqrt(x: int) -> int:
    lo, hi = 0, x + 1  # looking for the first k with k * k > x; k = x + 1 always is one
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if mid * mid > x:
            hi = mid
        else:
            lo = mid + 1
    return lo - 1


if __name__ == "__main__":
    assert integer_sqrt(17) == 4
    assert integer_sqrt(36) == 6
    assert integer_sqrt(0) == 0
    assert integer_sqrt(1) == 1
    assert integer_sqrt(2) == 1
    assert integer_sqrt(4) == 2
    assert integer_sqrt(2_147_395_599) == 46_339
    assert integer_sqrt(2_147_395_600) == 46_340
    assert integer_sqrt(2_147_483_647) == 46_340
    print("094 ok")
