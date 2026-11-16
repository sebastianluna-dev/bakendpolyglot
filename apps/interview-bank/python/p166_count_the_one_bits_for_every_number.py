# 166 · Count the One Bits for Every Number


def one_bit_counts(n: int) -> list[int]:
    bits = [0] * (n + 1)
    for i in range(1, n + 1):
        bits[i] = bits[i >> 1] + (i & 1)  # i without its last bit, plus that bit
    return bits


if __name__ == "__main__":
    assert one_bit_counts(5) == [0, 1, 1, 2, 1, 2]
    assert one_bit_counts(0) == [0]
    assert one_bit_counts(1) == [0, 1]
    assert one_bit_counts(8) == [0, 1, 1, 2, 1, 2, 2, 3, 1]
    assert one_bit_counts(1023)[1023] == 10
    assert one_bit_counts(100000)[65535] == 16
    print("166 ok")
