# 063 · Smallest Window Containing All Letters


def smallest_window(text: str, letters: str) -> str:
    need = [0] * 128  # copies still wanted, by character code
    for ch in letters:
        need[ord(ch)] += 1
    missing = len(letters)
    best_start, best_length = 0, len(text) + 1
    left = 0
    for right, ch in enumerate(text):
        c = ord(ch)
        if need[c] > 0:
            missing -= 1
        need[c] -= 1
        while missing == 0:
            if right - left + 1 < best_length:
                best_start, best_length = left, right - left + 1
            d = ord(text[left])
            need[d] += 1
            if need[d] > 0:
                missing += 1  # a wanted copy just left the window
            left += 1
    if best_length > len(text):
        return ""
    return text[best_start : best_start + best_length]


if __name__ == "__main__":
    assert smallest_window("deliverytruck", "tire") == "iveryt"
    assert smallest_window("banana", "nn") == "nan"
    assert smallest_window("abba", "ab") == "ab"
    assert smallest_window("abc", "abd") == ""
    assert smallest_window("a", "aa") == ""
    assert smallest_window("Aa", "a") == "a"
    assert smallest_window("", "a") == ""
    assert smallest_window("parcel", "parcel") == "parcel"
    print("063 ok")
