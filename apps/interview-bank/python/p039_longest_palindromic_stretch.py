# 039 · Longest Palindromic Stretch


def longest_palindrome(s: str) -> str:
    best_start, best_length = 0, 0
    for i in range(len(s)):
        for length in (expand(s, i, i), expand(s, i, i + 1)):
            if length > best_length:  # strictly longer, so the first stretch wins a tie
                best_length = length
                best_start = i - (length - 1) // 2
    return s[best_start : best_start + best_length]


def expand(s: str, lo: int, hi: int) -> int:
    while lo >= 0 and hi < len(s) and s[lo] == s[hi]:
        lo -= 1
        hi += 1
    return hi - lo - 1


if __name__ == "__main__":
    assert longest_palindrome("xabbaracecarz") == "racecar"
    assert longest_palindrome("abcde") == "a"
    assert longest_palindrome("xyzzyabba") == "yzzy"
    assert longest_palindrome("banana") == "anana"
    assert longest_palindrome("abacab") == "bacab"
    assert longest_palindrome("aaaa") == "aaaa"
    assert longest_palindrome("a") == "a"
    assert longest_palindrome("") == ""
    print("039 ok")
