# 017 · Longest Run of Consecutive IDs


def longest_run(ids: list[int]) -> int:
    present = set(ids)
    best = 0
    for member_id in present:
        if member_id - 1 in present:
            continue  # not the start of a run
        length = 1
        while member_id + length in present:
            length += 1
        best = max(best, length)
    return best


if __name__ == "__main__":
    assert longest_run([104, 7, 101, 5, 103, 6, 102]) == 4
    assert longest_run([9, 1, 9, 2, 2]) == 2
    assert longest_run([]) == 0
    assert longest_run([5]) == 1
    assert longest_run([-1, 0, 1, 3]) == 3
    assert longest_run([10, 30, 20]) == 1
    print("017 ok")
