# 043 · Say What You See


def say_what_you_see(start: str, rounds: int) -> str:
    s = start
    for _ in range(rounds):
        parts: list[str] = []
        i = 0
        while i < len(s):
            j = i
            while j < len(s) and s[j] == s[i]:
                j += 1
            parts.append(str(j - i) + s[i])
            i = j
        s = "".join(parts)
    return s


if __name__ == "__main__":
    assert say_what_you_see("1", 4) == "111221"
    assert say_what_you_see("3322251", 1) == "23321511"
    assert say_what_you_see("1", 0) == "1"
    assert say_what_you_see("22", 5) == "22"
    assert say_what_you_see("1111111111", 1) == "101"
    assert say_what_you_see("1", 9) == "13211311123113112211"
    print("043 ok")
