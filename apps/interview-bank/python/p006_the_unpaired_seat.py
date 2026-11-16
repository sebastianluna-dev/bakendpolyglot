# 006 · The Unpaired Seat


def unpaired_seat(log: list[int]) -> int:
    result = 0
    for seat in log:
        result ^= seat  # pairs cancel: x ^ x is 0
    return result


if __name__ == "__main__":
    assert unpaired_seat([14, 3, 14, 8, 3]) == 8
    assert unpaired_seat([21]) == 21
    assert unpaired_seat([4, 4, 9]) == 9
    assert unpaired_seat([7, 2, 2]) == 7
    assert unpaired_seat([1000000000, 5, 5]) == 1000000000
    print("006 ok")
