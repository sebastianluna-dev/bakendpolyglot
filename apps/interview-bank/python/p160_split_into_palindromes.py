# 160 · Split Into Palindromes


def palindrome_splits(s: str) -> list[list[str]]:
    result: list[list[str]] = []
    path: list[str] = []

    def is_palindrome(lo: int, hi: int) -> bool:
        while lo < hi:
            if s[lo] != s[hi]:
                return False
            lo += 1
            hi -= 1
        return True

    def walk(start: int) -> None:
        if start == len(s):
            result.append(path.copy())
            return
        for end in range(start, len(s)):
            if is_palindrome(start, end):
                path.append(s[start : end + 1])
                walk(end + 1)
                path.pop()

    walk(0)
    return result


if __name__ == "__main__":
    assert palindrome_splits("aab") == [["a", "a", "b"], ["aa", "b"]]
    assert palindrome_splits("noon") == [["n", "o", "o", "n"], ["n", "oo", "n"], ["noon"]]
    assert palindrome_splits("a") == [["a"]]
    assert palindrome_splits("ab") == [["a", "b"]]
    assert palindrome_splits("aaa") == [["a", "a", "a"], ["a", "aa"], ["aa", "a"], ["aaa"]]
    assert palindrome_splits("racecar")[-1] == ["racecar"]
    print("160 ok")
