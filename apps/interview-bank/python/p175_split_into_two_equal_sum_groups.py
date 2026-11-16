# 175 · Split Into Two Equal-Sum Groups


def can_split_evenly(weights: list[int]) -> bool:
    total = sum(weights)
    if total % 2 != 0:
        return False
    half = total // 2
    reachable = [False] * (half + 1)  # reachable[s]: some parcels seen so far weigh s
    reachable[0] = True
    for w in weights:
        for s in range(half, w - 1, -1):  # downwards, so this parcel is counted at most once
            if reachable[s - w]:
                reachable[s] = True
    return reachable[half]


if __name__ == "__main__":
    assert can_split_evenly([3, 1, 4, 2, 2]) is True
    assert can_split_evenly([2, 3, 7]) is False
    assert can_split_evenly([1]) is False
    assert can_split_evenly([5, 5]) is True
    assert can_split_evenly([1, 5]) is False
    assert can_split_evenly([1, 2, 4]) is False
    assert can_split_evenly([1, 2, 3, 4, 5, 6, 7]) is True
    assert can_split_evenly([6, 6, 6, 6, 6, 4]) is False
    assert can_split_evenly([100] * 200) is True
    print("175 ok")
