# 055 · Longest Stretch Without a Repeat


def longest_without_repeat(s: str) -> int:
    last: dict[str, int] = {}  # character -> index where it was last seen
    start = best = 0
    for i, ch in enumerate(s):
        prev = last.get(ch)
        if prev is not None and prev >= start:
            start = prev + 1  # only a repeat inside the window counts
        last[ch] = i
        best = max(best, i - start + 1)
    return best


if __name__ == "__main__":
    assert longest_without_repeat("shelfwise") == 7
    assert longest_without_repeat("aaaa") == 1
    assert longest_without_repeat("") == 0
    assert longest_without_repeat("abba") == 2
    assert longest_without_repeat("dvdf") == 3
    assert longest_without_repeat("A-1 a-2") == 5
    print("055 ok")
