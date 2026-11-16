# 070 · Remove Adjacent Pairs


def remove_pairs(s: str) -> str:
    kept: list[str] = []  # used as a stack: the result so far
    for ch in s:
        if kept and kept[-1] == ch:
            kept.pop()
        else:
            kept.append(ch)
    return "".join(kept)


if __name__ == "__main__":
    assert remove_pairs("abccbd") == "ad"
    assert remove_pairs("aabb") == ""
    assert remove_pairs("") == ""
    assert remove_pairs("abc") == "abc"
    assert remove_pairs("aaa") == "a"
    assert remove_pairs("abba") == ""
    assert remove_pairs("mississippi") == "m"
    print("070 ok")
