# 003 · Same Letters, Different Order


def same_letters(a: str, b: str) -> bool:
    if len(a) != len(b):
        return False
    counts = [0] * 26
    for x, y in zip(a, b):
        counts[ord(x) - ord("a")] += 1
        counts[ord(y) - ord("a")] -= 1
    return all(c == 0 for c in counts)


if __name__ == "__main__":
    assert same_letters("dusty", "study") is True
    assert same_letters("aab", "abb") is False
    assert same_letters("loan", "loans") is False
    assert same_letters("abc", "abc") is True
    assert same_letters("", "") is True
    print("003 ok")
