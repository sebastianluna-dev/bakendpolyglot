# 151 · Fast Power


def power_mod(x: int, n: int, m: int) -> int:
    if n == 0:
        return 1 % m
    half = power_mod(x, n // 2, m)  # one recursive call, used twice
    square = half * half % m
    return square if n % 2 == 0 else square * (x % m) % m


if __name__ == "__main__":
    assert power_mod(3, 13, 100) == 23
    assert power_mod(7, 0, 13) == 1
    assert power_mod(2, 10, 1000) == 24
    assert power_mod(5, 3, 1) == 0
    assert power_mod(0, 0, 7) == 1
    assert power_mod(0, 5, 7) == 0
    assert power_mod(999999999, 2, 1000000007) == 64
    assert power_mod(10, 9, 1000000007) == 1000000000
    assert power_mod(2, 1000000006, 1000000007) == 1
    print("151 ok")
