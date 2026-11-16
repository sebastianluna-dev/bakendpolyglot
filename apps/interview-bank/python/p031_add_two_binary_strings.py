# 031 · Add Two Binary Strings


def add_binary(a: str, b: str) -> str:
    digits: list[str] = []
    i, j, carry = len(a) - 1, len(b) - 1, 0
    while i >= 0 or j >= 0 or carry > 0:
        total = carry
        if i >= 0:
            total += ord(a[i]) - ord("0")
            i -= 1
        if j >= 0:
            total += ord(b[j]) - ord("0")
            j -= 1
        digits.append(str(total % 2))
        carry = total // 2
    return "".join(reversed(digits))


if __name__ == "__main__":
    assert add_binary("1011", "111") == "10010"
    assert add_binary("0", "0") == "0"
    assert add_binary("1", "1") == "10"
    assert add_binary("1111", "1") == "10000"
    assert add_binary("100", "110010") == "110110"
    assert add_binary("1" * 70, "1") == "1" + "0" * 70
    print("031 ok")
