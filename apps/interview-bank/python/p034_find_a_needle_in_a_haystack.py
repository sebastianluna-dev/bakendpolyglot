# 034 · Find a Needle in a Haystack


def find_needle(haystack: str, needle: str) -> int:
    for start in range(len(haystack) - len(needle) + 1):
        k = 0
        while k < len(needle) and haystack[start + k] == needle[k]:
            k += 1
        if k == len(needle):
            return start
    return -1


if __name__ == "__main__":
    assert find_needle("hold-14-seat-14", "14") == 5
    assert find_needle("parcel", "pulse") == -1
    assert find_needle("abc", "c") == 2
    assert find_needle("aaab", "ab") == 2
    assert find_needle("abc", "") == 0
    assert find_needle("", "") == 0
    assert find_needle("", "a") == -1
    assert find_needle("ab", "abc") == -1
    print("034 ok")
