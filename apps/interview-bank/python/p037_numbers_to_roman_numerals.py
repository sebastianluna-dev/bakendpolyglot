# 037 · Numbers to Roman Numerals

VALUES = [1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1]
SYMBOLS = ["M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"]


def to_roman(n: int) -> str:
    parts: list[str] = []
    for value, symbol in zip(VALUES, SYMBOLS):
        while n >= value:
            parts.append(symbol)
            n -= value
    return "".join(parts)


if __name__ == "__main__":
    assert to_roman(2026) == "MMXXVI"
    assert to_roman(1994) == "MCMXCIV"
    assert to_roman(1) == "I"
    assert to_roman(4) == "IV"
    assert to_roman(49) == "XLIX"
    assert to_roman(58) == "LVIII"
    assert to_roman(444) == "CDXLIV"
    assert to_roman(3999) == "MMMCMXCIX"
    print("037 ok")
