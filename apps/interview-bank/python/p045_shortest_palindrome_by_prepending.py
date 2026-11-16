# 045 · Shortest Palindrome by Prepending


def shortest_palindrome(s: str) -> str:
    reversed_s = s[::-1]
    t = s + "#" + reversed_s  # the '#' stops a match from running past the end of s
    fail = [0] * len(t)
    k = 0
    for i in range(1, len(t)):
        while k > 0 and t[i] != t[k]:
            k = fail[k - 1]
        if t[i] == t[k]:
            k += 1
        fail[i] = k
    keep = fail[-1]  # length of the longest palindromic prefix of s
    return reversed_s[: len(s) - keep] + s


if __name__ == "__main__":
    assert shortest_palindrome("abacd") == "dcabacd"
    assert shortest_palindrome("stats") == "stats"
    assert shortest_palindrome("") == ""
    assert shortest_palindrome("a") == "a"
    assert shortest_palindrome("ab") == "bab"
    assert shortest_palindrome("aabba") == "abbaabba"
    assert shortest_palindrome("aaa") == "aaa"
    print("045 ok")
