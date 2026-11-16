# 074 · Expand Repeated Patterns


def expand_patterns(s: str) -> str:
    counts: list[int] = []
    outer: list[str] = []  # the text built before each open bracket
    current = ""
    count = 0
    for ch in s:
        if ch.isdigit():
            count = count * 10 + int(ch)
        elif ch == "[":
            counts.append(count)
            outer.append(current)
            current = ""
            count = 0
        elif ch == "]":
            current = outer.pop() + current * counts.pop()
        else:
            current += ch
    return current


if __name__ == "__main__":
    assert expand_patterns("3[ab]2[c]") == "abababcc"
    assert expand_patterns("x2[y3[z]]") == "xyzzzyzzz"
    assert expand_patterns("") == ""
    assert expand_patterns("plain") == "plain"
    assert expand_patterns("12[q]") == "qqqqqqqqqqqq"
    assert expand_patterns("1[a1[b1[c]]]d") == "abcd"
    assert expand_patterns("2[a2[b]c]") == "abbcabbc"
    print("074 ok")
