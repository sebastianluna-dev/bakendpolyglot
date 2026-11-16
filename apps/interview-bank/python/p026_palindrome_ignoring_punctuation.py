# 026 · Palindrome, Ignoring Punctuation


def is_palindrome(text: str) -> bool:
    left, right = 0, len(text) - 1
    while left < right:
        if not text[left].isalnum():
            left += 1
        elif not text[right].isalnum():
            right -= 1
        elif text[left].lower() != text[right].lower():
            return False
        else:
            left += 1
            right -= 1
    return True


if __name__ == "__main__":
    assert is_palindrome("Step on no pets!") is True
    assert is_palindrome("Pets on no steps") is False
    assert is_palindrome("") is True
    assert is_palindrome(",.! ?") is True
    assert is_palindrome("a.") is True
    assert is_palindrome("Ab1bA") is True
    assert is_palindrome("1a2") is False
    assert is_palindrome("0P") is False
    print("026 ok")
