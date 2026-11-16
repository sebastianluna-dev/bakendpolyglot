# 042 · Multiply Numbers Written as Strings


def multiply_strings(a: str, b: str) -> str:
    digits = [0] * (len(a) + len(b))
    for i in range(len(a) - 1, -1, -1):
        for j in range(len(b) - 1, -1, -1):
            total = int(a[i]) * int(b[j]) + digits[i + j + 1]
            digits[i + j + 1] = total % 10
            digits[i + j] += total // 10  # may pass 9 for now; fixed when its own turn comes
    start = 0
    while start < len(digits) - 1 and digits[start] == 0:
        start += 1
    return "".join(str(d) for d in digits[start:])


if __name__ == "__main__":
    assert multiply_strings("123", "45") == "5535"
    assert multiply_strings("0", "9081") == "0"
    assert multiply_strings("999", "999") == "998001"
    assert multiply_strings("7", "8") == "56"
    assert multiply_strings("1", "1") == "1"
    assert multiply_strings("123456789", "987654321") == "121932631112635269"
    assert (
        multiply_strings("99999999999999999999", "99999999999999999999")
        == "9999999999999999999800000000000000000001"
    )
    print("042 ok")
