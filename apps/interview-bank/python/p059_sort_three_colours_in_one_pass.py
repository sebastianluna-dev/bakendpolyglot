# 059 · Sort Three Colours in One Pass


def sort_colours(stickers: list[int]) -> None:
    low = mid = 0  # [0, low) is 0, [low, mid) is 1
    high = len(stickers) - 1  # (high, end] is 2
    while mid <= high:
        if stickers[mid] == 0:
            stickers[low], stickers[mid] = stickers[mid], stickers[low]
            low += 1
            mid += 1
        elif stickers[mid] == 1:
            mid += 1
        else:
            stickers[mid], stickers[high] = stickers[high], stickers[mid]
            high -= 1  # mid stays: the value swapped in has not been looked at yet


if __name__ == "__main__":
    cases = [
        ([2, 0, 1, 2, 1, 0], [0, 0, 1, 1, 2, 2]),
        ([1, 1, 1], [1, 1, 1]),
        ([], []),
        ([2, 0], [0, 2]),
        ([2, 2, 0, 0], [0, 0, 2, 2]),
        ([0, 2, 1], [0, 1, 2]),
        ([1, 2, 0], [0, 1, 2]),
        ([1, 2, 0, 2, 0, 1, 0], [0, 0, 0, 1, 1, 2, 2]),
    ]
    for stickers, want in cases:
        sort_colours(stickers)
        assert stickers == want
    print("059 ok")
