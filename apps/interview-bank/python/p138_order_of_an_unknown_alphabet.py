# 138 · Order of an Unknown Alphabet


def alphabet_order(words: list[str]) -> str:
    present = [False] * 26
    for w in words:
        for ch in w:
            present[ord(ch) - ord("a")] = True
    before = [[False] * 26 for _ in range(26)]  # before[x][y]: x comes before y
    waiting = [0] * 26  # letters that must come before this one and are not placed yet
    for a, b in zip(words, words[1:]):
        j = 0
        while j < len(a) and j < len(b) and a[j] == b[j]:
            j += 1
        if j == len(a) or j == len(b):
            if len(a) > len(b):
                return ""  # a word sorted before its own prefix
            continue
        x, y = ord(a[j]) - ord("a"), ord(b[j]) - ord("a")
        if not before[x][y]:
            before[x][y] = True
            waiting[y] += 1
    placed = [False] * 26
    order: list[str] = []
    while True:
        x = 0
        while x < 26 and not (present[x] and not placed[x] and waiting[x] == 0):
            x += 1
        if x == 26:
            break
        placed[x] = True
        order.append(chr(ord("a") + x))
        for y in range(26):
            if before[x][y]:
                waiting[y] -= 1
    return "".join(order) if len(order) == sum(present) else ""  # shorter means a cycle


if __name__ == "__main__":
    assert alphabet_order(["cab", "cad", "ab", "ba", "bd"]) == "cabd"
    assert alphabet_order(["ba", "bc", "ad"]) == "bacd"
    assert alphabet_order(["abc", "ab"]) == ""
    assert alphabet_order(["a", "b", "a"]) == ""
    assert alphabet_order(["a", "b", "c", "a"]) == ""
    assert alphabet_order(["zyx"]) == "xyz"
    assert alphabet_order(["ab", "ab"]) == "ab"
    assert alphabet_order(["ab", "abc"]) == "abc"
    assert alphabet_order(["x", "z", "y"]) == "xzy"
    print("138 ok")
