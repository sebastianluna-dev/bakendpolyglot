# 032 · Run-Length Encode a String


def run_length_encode(s: str) -> str:
    parts: list[str] = []
    i = 0
    while i < len(s):
        j = i
        while j < len(s) and s[j] == s[i]:
            j += 1
        parts.append(s[i] + str(j - i))
        i = j
    return "".join(parts)


if __name__ == "__main__":
    assert run_length_encode("aaabccdddd") == "a3b1c2d4"
    assert run_length_encode("abc") == "a1b1c1"
    assert run_length_encode("") == ""
    assert run_length_encode("z" * 12) == "z12"
    assert run_length_encode("aAaa") == "a1A1a2"
    assert run_length_encode("abb") == "a1b2"
    print("032 ok")
