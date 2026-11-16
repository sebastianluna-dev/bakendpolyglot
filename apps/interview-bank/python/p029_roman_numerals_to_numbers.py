# 029 · Roman Numerals to Numbers

VALUES = {"I": 1, "V": 5, "X": 10, "L": 50, "C": 100, "D": 500, "M": 1000}


def roman_to_number(roman: str) -> int:
    total = 0
    for i, ch in enumerate(roman):
        value = VALUES[ch]
        next_value = VALUES[roman[i + 1]] if i + 1 < len(roman) else 0
        # a smaller symbol before a larger one is subtracted (IV = 5 - 1)
        total += -value if value < next_value else value
    return total


if __name__ == "__main__":
    assert roman_to_number("MMXXVI") == 2026
    assert roman_to_number("CDXLIV") == 444
    assert roman_to_number("I") == 1
    assert roman_to_number("IV") == 4
    assert roman_to_number("IX") == 9
    assert roman_to_number("XIV") == 14
    assert roman_to_number("CM") == 900
    assert roman_to_number("MMMCMXCIX") == 3999
    print("029 ok")
