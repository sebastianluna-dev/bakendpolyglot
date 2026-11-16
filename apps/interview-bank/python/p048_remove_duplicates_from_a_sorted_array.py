# 048 · Remove Duplicates From a Sorted Array


def remove_duplicates(ids: list[int]) -> int:
    if not ids:
        return 0
    write = 1  # ids[:write] holds the distinct ids kept so far
    for read in range(1, len(ids)):
        if ids[read] != ids[write - 1]:
            ids[write] = ids[read]
            write += 1
    return write


def deduped(ids: list[int]) -> list[int]:
    k = remove_duplicates(ids)
    return ids[:k]


if __name__ == "__main__":
    assert deduped([3, 3, 5, 8, 8, 8, 9]) == [3, 5, 8, 9]
    assert deduped([7, 7, 7]) == [7]
    assert deduped([]) == []
    assert deduped([1, 2, 3]) == [1, 2, 3]
    assert deduped([10, 10, 20, 20, 30]) == [10, 20, 30]
    print("048 ok")
