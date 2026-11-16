# 013 · Collapse Consecutive IDs Into Ranges


def collapse_ranges(ids: list[int]) -> list[str]:
    ranges: list[str] = []
    i = 0
    while i < len(ids):
        start = ids[i]
        while i + 1 < len(ids) and ids[i + 1] == ids[i] + 1:
            i += 1
        ranges.append(str(start) if start == ids[i] else f"{start}-{ids[i]}")
        i += 1
    return ranges


if __name__ == "__main__":
    assert collapse_ranges([1, 2, 3, 7, 9, 10]) == ["1-3", "7", "9-10"]
    assert collapse_ranges([0, 2, 4]) == ["0", "2", "4"]
    assert collapse_ranges([]) == []
    assert collapse_ranges([5]) == ["5"]
    assert collapse_ranges([40, 41, 42, 43]) == ["40-43"]
    print("013 ok")
