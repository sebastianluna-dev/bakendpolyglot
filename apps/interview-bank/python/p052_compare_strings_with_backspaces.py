# 052 · Compare Strings With Backspaces


def same_after_backspaces(a: str, b: str) -> bool:
    i, j = len(a) - 1, len(b) - 1
    while i >= 0 or j >= 0:
        i = last_kept(a, i)
        j = last_kept(b, j)
        if i < 0 or j < 0:
            return i < 0 and j < 0
        if a[i] != b[j]:
            return False
        i -= 1
        j -= 1
    return True


def last_kept(s: str, i: int) -> int:
    """Index of the last character at or before i that no later '#' deletes, or -1."""
    skip = 0
    while i >= 0:
        if s[i] == "#":
            skip += 1
        elif skip > 0:
            skip -= 1
        else:
            break
        i -= 1
    return i


if __name__ == "__main__":
    assert same_after_backspaces("A1#2", "A2")
    assert not same_after_backspaces("B7##C", "C#B")
    assert same_after_backspaces("##", "")
    assert same_after_backspaces("ab##", "c#d#")
    assert same_after_backspaces("x#y", "y")
    assert not same_after_backspaces("abc", "ab")
    assert not same_after_backspaces("a", "ba")
    assert same_after_backspaces("a#b#c", "#c")
    print("052 ok")
