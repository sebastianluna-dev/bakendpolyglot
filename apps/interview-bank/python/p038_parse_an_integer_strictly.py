# 038 · Parse an Integer Strictly


def parse_integer(s: str) -> int | None:
    i = 0
    negative = False
    if s[:1] in ("+", "-"):
        negative = s[0] == "-"
        i = 1
    if i == len(s):
        return None  # empty, or a sign with no digits
    limit = 2**31 if negative else 2**31 - 1
    value = 0
    for ch in s[i:]:
        if not "0" <= ch <= "9":
            return None
        value = value * 10 + (ord(ch) - ord("0"))
        if value > limit:
            return None
    return -value if negative else value


if __name__ == "__main__":
    assert parse_integer("42") == 42
    assert parse_integer("-17") == -17
    assert parse_integer("+8") == 8
    assert parse_integer("0042") == 42
    assert parse_integer("-0") == 0
    assert parse_integer("2147483647") == 2147483647
    assert parse_integer("-2147483648") == -2147483648
    assert parse_integer("2147483648") is None
    assert parse_integer("-2147483649") is None
    assert parse_integer("99999999999999999999") is None
    assert parse_integer("") is None
    assert parse_integer("+") is None
    assert parse_integer(" 42") is None
    assert parse_integer("42 ") is None
    assert parse_integer("12a") is None
    assert parse_integer("+-3") is None
    assert parse_integer("3.0") is None
    print("038 ok")
