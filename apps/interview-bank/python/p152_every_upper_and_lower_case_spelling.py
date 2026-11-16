# 152 · Every Upper- and Lower-Case Spelling


def all_spellings(code: str) -> list[str]:
    chars = list(code)
    out: list[str] = []

    def spell(i: int) -> None:
        if i == len(chars):
            out.append("".join(chars))
            return
        c = code[i]
        if not c.isalpha():  # a digit: one choice
            spell(i + 1)
            return
        chars[i] = c.upper()  # upper case first keeps the output sorted
        spell(i + 1)
        chars[i] = c.lower()
        spell(i + 1)

    spell(0)
    return out


if __name__ == "__main__":
    assert all_spellings("b7x") == ["B7X", "B7x", "b7X", "b7x"]
    assert all_spellings("42") == ["42"]
    assert all_spellings("Q") == ["Q", "q"]
    assert all_spellings("aB1") == ["AB1", "Ab1", "aB1", "ab1"]
    assert all_spellings("0z0") == ["0Z0", "0z0"]
    assert len(all_spellings("abcdefghij")) == 1024
    print("152 ok")
