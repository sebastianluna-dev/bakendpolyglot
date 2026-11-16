# 047 · Merge Two Sorted Arrays in Place


def merge_in_place(a: list[int], m: int, b: list[int]) -> None:
    i, j = m - 1, len(b) - 1
    k = m + len(b) - 1  # next slot to fill, from the back
    while j >= 0:
        if i >= 0 and a[i] > b[j]:
            a[k] = a[i]
            i -= 1
        else:
            a[k] = b[j]
            j -= 1
        k -= 1


def merged(a: list[int], m: int, b: list[int]) -> list[int]:
    merge_in_place(a, m, b)
    return a


if __name__ == "__main__":
    assert merged([1, 4, 7, 0, 0], 3, [2, 8]) == [1, 2, 4, 7, 8]
    assert merged([0, 0], 0, [3, 5]) == [3, 5]
    assert merged([2, 3], 2, []) == [2, 3]
    assert merged([1, 2, 2, 0, 0, 0], 3, [2, 2, 3]) == [1, 2, 2, 2, 2, 3]
    assert merged([-3, 5, 0], 2, [-4]) == [-4, -3, 5]
    assert merged([4, 5, 0], 2, [1]) == [1, 4, 5]
    print("047 ok")
