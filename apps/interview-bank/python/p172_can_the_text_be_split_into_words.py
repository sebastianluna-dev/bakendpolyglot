# 172 · Can the Text Be Split Into Words?


def can_split(text: str, words: list[str]) -> bool:
    dictionary = set(words)
    longest = max((len(w) for w in words), default=0)
    splits = [False] * (len(text) + 1)  # splits[i]: the first i letters split into words
    splits[0] = True
    for end in range(1, len(text) + 1):
        for length in range(1, min(end, longest) + 1):
            if splits[end - length] and text[end - length:end] in dictionary:
                splits[end] = True
                break
    return splits[len(text)]


if __name__ == "__main__":
    assert can_split("bookseller", ["books", "book", "seller"]) is True
    assert can_split("trucking", ["truck", "king", "in"]) is False
    assert can_split("", ["a"]) is True
    assert can_split("abcd", []) is False
    assert can_split("seatseat", ["seat"]) is True
    assert can_split("aaaaaaa", ["aaaa", "aaa"]) is True
    assert can_split("aaaaaaaaab", ["a", "aa"]) is False
    print("172 ok")
