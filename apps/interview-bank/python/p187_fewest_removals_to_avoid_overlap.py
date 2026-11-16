# 187 · Fewest Removals to Avoid Overlap


def fewest_removals(intervals: list[list[int]]) -> int:
    removals = 0
    last_end = float("-inf")  # end of the last interval kept
    for start, end in sorted(intervals, key=lambda iv: iv[1]):
        if start >= last_end:
            last_end = end
        else:
            removals += 1
    return removals


if __name__ == "__main__":
    assert fewest_removals([[1, 10], [2, 3], [4, 5], [6, 7]]) == 1
    assert fewest_removals([[1, 3], [3, 5], [5, 8]]) == 0
    assert fewest_removals([]) == 0
    assert fewest_removals([[1, 2], [1, 2], [1, 2]]) == 2
    assert fewest_removals([[1, 4], [2, 3], [3, 6], [5, 7]]) == 2
    print("187 ok")
