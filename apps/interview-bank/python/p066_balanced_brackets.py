# 066 · Balanced Brackets

CLOSER = {"(": ")", "[": "]", "{": "}"}


def is_balanced(s: str) -> bool:
    expected: list[str] = []  # the closer each open bracket is waiting for
    for ch in s:
        if ch in CLOSER:
            expected.append(CLOSER[ch])
        elif ch in ")]}":
            if not expected or expected.pop() != ch:
                return False
    return not expected


if __name__ == "__main__":
    assert is_balanced("lookup(ids[0], {limit: 5})") is True
    assert is_balanced("([)]") is False
    assert is_balanced("") is True
    assert is_balanced("no brackets") is True
    assert is_balanced("(") is False
    assert is_balanced(")") is False
    assert is_balanced("}{") is False
    assert is_balanced("((") is False
    assert is_balanced("{[()()]}") is True
    print("066 ok")
