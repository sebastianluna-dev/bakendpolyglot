# 164 · Rob Houses Without Tripping the Alarm


def max_haul(cash: list[int]) -> int:
    skip_prev, best = 0, 0  # best totals up to two houses back and up to here
    for c in cash:
        skip_prev, best = best, max(best, skip_prev + c)
    return best


if __name__ == "__main__":
    assert max_haul([6, 1, 2, 9]) == 15
    assert max_haul([5, 10, 4, 3, 8]) == 18
    assert max_haul([]) == 0
    assert max_haul([7]) == 7
    assert max_haul([2, 1, 1, 2]) == 4
    assert max_haul([4, 1, 2, 7, 5, 3, 1]) == 14
    assert max_haul([0, 0, 0]) == 0
    print("164 ok")
