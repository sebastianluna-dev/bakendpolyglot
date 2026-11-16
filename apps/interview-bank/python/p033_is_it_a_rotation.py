# 033 · Is It a Rotation?


def is_rotation(a: str, b: str) -> bool:
    return len(a) == len(b) and b in a + a


if __name__ == "__main__":
    assert is_rotation("parcel", "celpar")
    assert not is_rotation("parcel", "parcle")
    assert is_rotation("abc", "abc")
    assert is_rotation("", "")
    assert not is_rotation("aa", "a")
    assert is_rotation("abab", "baba")
    print("033 ok")
