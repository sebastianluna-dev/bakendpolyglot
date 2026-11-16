# 178 · Pattern Matching With . and *


def matches_pattern(text: str, pattern: str) -> bool:
    m = len(pattern)
    # cur[j]: the first i letters of text match the first j characters of pattern; prev holds row i - 1
    prev = [False] * (m + 1)
    for i in range(len(text) + 1):
        cur = [False] * (m + 1)
        cur[0] = i == 0
        for j in range(1, m + 1):
            if pattern[j - 1] == "*":
                # the starred item matches nothing, or it takes text[i - 1] and may take more
                cur[j] = cur[j - 2] or (i > 0 and fits(text[i - 1], pattern[j - 2]) and prev[j])
            else:
                cur[j] = i > 0 and fits(text[i - 1], pattern[j - 1]) and prev[j - 1]
        prev = cur
    return prev[m]


def fits(c: str, p: str) -> bool:
    return p == "." or p == c


if __name__ == "__main__":
    assert matches_pattern("seat", "se.*t") is True
    assert matches_pattern("seats", "se.*t") is False
    assert matches_pattern("", "a*b*") is True
    assert matches_pattern("", "") is True
    assert matches_pattern("a", "") is False
    assert matches_pattern("", ".") is False
    assert matches_pattern("xxy", "z*x*y") is True
    assert matches_pattern("ac", "a.c") is False
    assert matches_pattern("aaa", "a*a") is True
    assert matches_pattern("bbb", "bc*b*d*b") is True
    assert matches_pattern("b", "a*b") is True
    assert matches_pattern("ab", ".*") is True
    assert matches_pattern("abcd", "d*") is False
    assert matches_pattern("aaaaaaaaaaaaaaaaaaab", "a*a*a*a*a*a*a*a*c") is False
    print("178 ok")
