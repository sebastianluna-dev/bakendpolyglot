# 180 · Count Distinct Subsequences

MOD = 1_000_000_007


def count_subsequences(s: str, t: str) -> int:
    m = len(t)
    ways = [0] * (m + 1)  # ways[j]: ways to spell the first j letters of t so far
    ways[0] = 1
    for c in s:
        for j in range(m, 0, -1):  # downwards, so this letter of s is used at most once
            if t[j - 1] == c:
                ways[j] = (ways[j] + ways[j - 1]) % MOD
    return ways[m]


if __name__ == "__main__":
    assert count_subsequences("banana", "ban") == 3
    assert count_subsequences("seatseat", "sat") == 4
    assert count_subsequences("abc", "abcd") == 0
    assert count_subsequences("abc", "") == 1
    assert count_subsequences("", "a") == 0
    assert count_subsequences("", "") == 1
    assert count_subsequences("aaaa", "aa") == 6
    assert count_subsequences("a", "aa") == 0
    assert count_subsequences("a" * 1000, "a" * 500) == 159835829
    print("180 ok")
