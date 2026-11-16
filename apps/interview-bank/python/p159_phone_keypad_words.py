# 159 · Phone Keypad Words

KEYS = ["", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"]


def keypad_words(digits: str) -> list[str]:
    if not digits:
        return []
    result: list[str] = []
    path: list[str] = []

    def walk(k: int) -> None:
        if k == len(digits):
            result.append("".join(path))
            return
        for letter in KEYS[int(digits[k])]:
            path.append(letter)
            walk(k + 1)
            path.pop()

    walk(0)
    return result


if __name__ == "__main__":
    assert keypad_words("23") == ["ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"]
    assert keypad_words("7") == ["p", "q", "r", "s"]
    assert keypad_words("") == []
    assert keypad_words("9") == ["w", "x", "y", "z"]
    words = keypad_words("79")
    assert len(words) == 16 and words[0] == "pw" and words[-1] == "sz"
    assert len(keypad_words("234")) == 27
    print("159 ok")
