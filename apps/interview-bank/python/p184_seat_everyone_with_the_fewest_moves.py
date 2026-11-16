# 184 · Seat Everyone With the Fewest Moves


def fewest_moves(seats: list[int], guests: list[int]) -> int:
    return sum(abs(s - g) for s, g in zip(sorted(seats), sorted(guests)))


if __name__ == "__main__":
    assert fewest_moves([4, 1, 7], [2, 8, 3]) == 3
    assert fewest_moves([2, 6, 9, 10], [5, 5, 1, 12]) == 8
    assert fewest_moves([], []) == 0
    assert fewest_moves([3, 5], [5, 3]) == 0
    assert fewest_moves([10], [1]) == 9
    print("184 ok")
