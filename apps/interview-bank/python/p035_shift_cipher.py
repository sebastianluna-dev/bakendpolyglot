# 035 · Shift Cipher


def shift_cipher(text: str, shift: int) -> str:
    k = shift % 26  # Python's % is never negative for a positive divisor: -1 % 26 is 25
    chars = list(text)
    for i, ch in enumerate(chars):
        if "a" <= ch <= "z":
            chars[i] = chr(ord("a") + (ord(ch) - ord("a") + k) % 26)
        elif "A" <= ch <= "Z":
            chars[i] = chr(ord("A") + (ord(ch) - ord("A") + k) % 26)
    return "".join(chars)


if __name__ == "__main__":
    assert shift_cipher("Hold seat 14!", 3) == "Krog vhdw 14!"
    assert shift_cipher("xyz", 2) == "zab"
    assert shift_cipher("Abc", -1) == "Zab"
    assert shift_cipher("abc", 52) == "abc"
    assert shift_cipher("Zz", -27) == "Yy"
    assert shift_cipher("Krog vhdw 14!", -3) == "Hold seat 14!"
    assert shift_cipher("", 5) == ""
    print("035 ok")
