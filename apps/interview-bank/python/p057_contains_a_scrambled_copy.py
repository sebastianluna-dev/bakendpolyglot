# 057 · Contains a Scrambled Copy


def contains_scrambled(pattern: str, text: str) -> bool:
    m = len(pattern)
    if m > len(text):
        return False
    need = [0] * 26
    have = [0] * 26
    for i in range(m):
        need[ord(pattern[i]) - ord("a")] += 1
        have[ord(text[i]) - ord("a")] += 1
    if need == have:
        return True
    for i in range(m, len(text)):
        have[ord(text[i]) - ord("a")] += 1  # letter entering the window
        have[ord(text[i - m]) - ord("a")] -= 1  # letter leaving it
        if need == have:
            return True
    return False


if __name__ == "__main__":
    assert contains_scrambled("loan", "renewalnoted")
    assert not contains_scrambled("seat", "stage")
    assert not contains_scrambled("abc", "ab")
    assert contains_scrambled("ab", "ab")
    assert contains_scrambled("aab", "baxaba")
    assert not contains_scrambled("aab", "abbb")
    assert contains_scrambled("z", "abcz")
    print("057 ok")
