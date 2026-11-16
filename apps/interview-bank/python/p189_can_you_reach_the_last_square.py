# 189 · Can You Reach the Last Square?


def can_reach_last(jumps: list[int]) -> bool:
    furthest = 0
    for i, jump in enumerate(jumps):
        if i > furthest:
            return False  # no reachable square jumps this far
        furthest = max(furthest, i + jump)
    return True


if __name__ == "__main__":
    assert can_reach_last([2, 0, 3, 0, 0, 1]) is True
    assert can_reach_last([3, 1, 1, 0, 4]) is False
    assert can_reach_last([0]) is True
    assert can_reach_last([0, 1]) is False
    assert can_reach_last([4, 0, 0, 0, 0]) is True
    assert can_reach_last([1, 1, 0, 1]) is False
    assert can_reach_last([1, 2, 0, 0]) is True
    print("189 ok")
