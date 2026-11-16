# 018 · Count Stretches With a Target Sum


def count_stretches(changes: list[int], target: int) -> int:
    seen: dict[int, int] = {0: 1}  # prefix sum -> how many times; 0 is the empty prefix
    total = 0
    count = 0
    for change in changes:
        total += change
        count += seen.get(total - target, 0)
        seen[total] = seen.get(total, 0) + 1
    return count


if __name__ == "__main__":
    assert count_stretches([1, 2, 3, -2, 2], 3) == 4
    assert count_stretches([0, 0, 0], 0) == 6
    assert count_stretches([], 0) == 0
    assert count_stretches([5], 3) == 0
    assert count_stretches([3], 3) == 1
    assert count_stretches([1, -1, 1, -1], 0) == 4
    print("018 ok")
