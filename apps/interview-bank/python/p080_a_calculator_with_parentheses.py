# 080 · A Calculator With Parentheses


def calculate(s: str) -> int:
    result = 0
    sign = 1
    number = 0
    stack: list[int] = []  # for each open bracket: the result before it, then the sign in front of it
    for ch in s:
        if ch.isdigit():
            number = number * 10 + int(ch)
        elif ch in "+-":
            result += sign * number
            number = 0
            sign = 1 if ch == "+" else -1
        elif ch == "(":
            stack.append(result)
            stack.append(sign)
            result = 0
            sign = 1
        elif ch == ")":
            result += sign * number
            number = 0
            outer_sign = stack.pop()
            result = stack.pop() + outer_sign * result
    return result + sign * number


if __name__ == "__main__":
    assert calculate("7 - (3 - (1 + 2))") == 7
    assert calculate("-(4 - (-2 + 9)) + 10") == 13
    assert calculate("12") == 12
    assert calculate(" 1 + 2 ") == 3
    assert calculate("-5") == -5
    assert calculate("(((3)))") == 3
    assert calculate("10 - 2 - 3") == 5
    assert calculate("1 - (2 + 3)") == -4
    assert calculate("-(-(-1))") == -1
    assert calculate("2147483647") == 2147483647
    print("080 ok")
