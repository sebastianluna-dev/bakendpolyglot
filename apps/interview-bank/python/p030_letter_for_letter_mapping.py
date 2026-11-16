# 030 · Letter-for-Letter Mapping


def can_map_letters(source: str, target: str) -> bool:
    if len(source) != len(target):
        return False
    forward: dict[str, str] = {}
    backward: dict[str, str] = {}  # stops two letters mapping to the same one
    for a, b in zip(source, target):
        if forward.get(a, b) != b or backward.get(b, a) != a:
            return False
        forward[a] = b
        backward[b] = a
    return True


if __name__ == "__main__":
    assert can_map_letters("deed", "noon") is True
    assert can_map_letters("bell", "cool") is False
    assert can_map_letters("", "") is True
    assert can_map_letters("a", "bc") is False
    assert can_map_letters("abc", "bca") is True
    assert can_map_letters("ab", "aa") is False
    assert can_map_letters("aa", "ab") is False
    assert can_map_letters("tab", "tat") is False
    print("030 ok")
