# 014 · Group Words by Their Letters


def group_by_letters(words: list[str]) -> list[list[str]]:
    group_of: dict[str, int] = {}  # sorted letters -> index in groups
    groups: list[list[str]] = []
    for word in words:
        key = "".join(sorted(word))
        if key not in group_of:
            group_of[key] = len(groups)
            groups.append([])
        groups[group_of[key]].append(word)
    return groups


if __name__ == "__main__":
    assert group_by_letters(["listen", "stone", "silent", "notes", "enlist", "tones", "tab"]) == [
        ["listen", "silent", "enlist"],
        ["stone", "notes", "tones"],
        ["tab"],
    ]
    assert group_by_letters(["loop", "pool", "polo", "lop"]) == [["loop", "pool", "polo"], ["lop"]]
    assert group_by_letters([]) == []
    assert group_by_letters(["ab", "ba", "ab"]) == [["ab", "ba", "ab"]]
    assert group_by_letters(["", "a", ""]) == [["", ""], ["a"]]
    print("014 ok")
