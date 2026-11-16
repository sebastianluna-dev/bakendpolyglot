# 173 · Ways to Decode a Digit String


def count_decodings(digits: str) -> int:
    two_back = 0  # decodings of the first i - 2 digits
    one_back = 1  # decodings of the first i - 1 digits; the empty prefix has one
    for i in range(1, len(digits) + 1):
        ways = one_back if digits[i - 1] != "0" else 0
        if i >= 2 and 10 <= int(digits[i - 2 : i]) <= 26:  # at least 10: "06" is not a letter
            ways += two_back
        two_back, one_back = one_back, ways
    return one_back


if __name__ == "__main__":
    assert count_decodings("2612") == 4
    assert count_decodings("301") == 0
    assert count_decodings("1201") == 1
    assert count_decodings("106") == 1
    assert count_decodings("0") == 0
    assert count_decodings("100") == 0
    assert count_decodings("27") == 1
    assert count_decodings("11106") == 2
    assert count_decodings("1" * 45) == 1836311903
    print("173 ok")
