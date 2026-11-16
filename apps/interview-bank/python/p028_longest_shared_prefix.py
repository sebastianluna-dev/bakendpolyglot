# 028 · Longest Shared Prefix


def longest_shared_prefix(words: list[str]) -> str:
    if not words:
        return ""
    first = words[0]
    for i in range(len(first)):
        for word in words:
            if i == len(word) or word[i] != first[i]:
                return first[:i]
    return first


if __name__ == "__main__":
    assert longest_shared_prefix(["QA76.73", "QA76.76", "QA76.9"]) == "QA76."
    assert longest_shared_prefix(["parcel", "pulse", "seat"]) == ""
    assert longest_shared_prefix([]) == ""
    assert longest_shared_prefix(["solo"]) == "solo"
    assert longest_shared_prefix(["ab", "a"]) == "a"
    assert longest_shared_prefix(["", "x"]) == ""
    assert longest_shared_prefix(["hold", "hold"]) == "hold"
    print("028 ok")
