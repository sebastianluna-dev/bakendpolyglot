# 177 · Edit Distance


def edit_distance(source: str, target: str) -> int:
    m = len(target)
    # cur[j]: fewest edits turning the first i letters of source into the first j of target; prev holds row i - 1
    prev = list(range(m + 1))
    for i in range(1, len(source) + 1):
        cur = [i] + [0] * m
        for j in range(1, m + 1):
            if source[i - 1] == target[j - 1]:
                cur[j] = prev[j - 1]
            else:
                cur[j] = 1 + min(prev[j], cur[j - 1], prev[j - 1])  # delete, insert, replace
        prev = cur
    return prev[m]


if __name__ == "__main__":
    assert edit_distance("shelf", "shelves") == 3
    assert edit_distance("loan", "lone") == 2
    assert edit_distance("", "abc") == 3
    assert edit_distance("abc", "") == 3
    assert edit_distance("seat", "seat") == 0
    assert edit_distance("parcel", "pencil") == 3
    assert edit_distance("trucks", "truck") == 1
    assert edit_distance("abc", "cab") == 2
    assert edit_distance("booking", "looking") == 1
    print("177 ok")
