# 060 · Two Kinds of Fruit


def longest_two_kinds(crates: list[int]) -> int:
    in_window: dict[int, int] = {}  # kind -> crates of it in the window
    left = best = 0
    for right, kind in enumerate(crates):
        in_window[kind] = in_window.get(kind, 0) + 1
        while len(in_window) > 2:
            out = crates[left]
            in_window[out] -= 1
            if in_window[out] == 0:
                del in_window[out]  # a kind with no crates left must not count
            left += 1
        best = max(best, right - left + 1)
    return best


if __name__ == "__main__":
    assert longest_two_kinds([3, 3, 7, 3, 9, 9, 7]) == 4
    assert longest_two_kinds([5, 5, 5]) == 3
    assert longest_two_kinds([]) == 0
    assert longest_two_kinds([1, 2, 3, 4]) == 2
    assert longest_two_kinds([1, 2, 1, 3, 3, 3, 1]) == 5
    assert longest_two_kinds([4]) == 1
    print("060 ok")
