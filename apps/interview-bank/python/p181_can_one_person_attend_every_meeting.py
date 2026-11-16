# 181 · Can One Person Attend Every Meeting?


def can_attend_all(meetings: list[list[int]]) -> bool:
    meetings.sort(key=lambda m: m[0])
    for i in range(1, len(meetings)):
        if meetings[i][0] < meetings[i - 1][1]:  # starting just as the last one ends is fine
            return False
    return True


if __name__ == "__main__":
    assert can_attend_all([[540, 600], [600, 660], [480, 540]]) is True
    assert can_attend_all([[540, 630], [600, 660]]) is False
    assert can_attend_all([]) is True
    assert can_attend_all([[100, 200]]) is True
    assert can_attend_all([[700, 800], [100, 200], [150, 160]]) is False
    assert can_attend_all([[5, 10], [5, 6]]) is False
    assert can_attend_all([[0, 1], [2, 3], [1, 2]]) is True
    print("181 ok")
