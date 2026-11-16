# 174 · Longest Common Subsequence


def longest_common_subsequence(a: str, b: str) -> int:
    m = len(b)
    # cur[j]: answer for the first i letters of a and the first j of b; prev holds row i - 1
    prev = [0] * (m + 1)
    for i in range(1, len(a) + 1):
        cur = [0] * (m + 1)
        for j in range(1, m + 1):
            if a[i - 1] == b[j - 1]:
                cur[j] = prev[j - 1] + 1
            else:
                cur[j] = max(prev[j], cur[j - 1])
        prev = cur
    return prev[m]


if __name__ == "__main__":
    assert longest_common_subsequence("parcel", "place") == 4
    assert longest_common_subsequence("seat", "lock") == 0
    assert longest_common_subsequence("", "abc") == 0
    assert longest_common_subsequence("abc", "") == 0
    assert longest_common_subsequence("member", "member") == 6
    assert longest_common_subsequence("aaaa", "aa") == 2
    assert longest_common_subsequence("delivery", "relived") == 5
    print("174 ok")
