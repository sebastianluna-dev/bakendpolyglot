# 027 · First Letter That Appears Once


def first_unique_index(word: str) -> int:
    counts = [0] * 26
    for ch in word:
        counts[ord(ch) - ord("a")] += 1
    for i, ch in enumerate(word):
        if counts[ord(ch) - ord("a")] == 1:
            return i
    return -1


if __name__ == "__main__":
    assert first_unique_index("shelfwise") == 1
    assert first_unique_index("noon") == -1
    assert first_unique_index("") == -1
    assert first_unique_index("z") == 0
    assert first_unique_index("aabbc") == 4
    assert first_unique_index("pear") == 0
    print("027 ok")
