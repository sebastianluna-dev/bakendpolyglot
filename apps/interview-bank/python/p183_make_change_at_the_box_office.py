# 183 · Make Change at the Box Office


def can_make_change(bills: list[int]) -> bool:
    fives = tens = 0
    for bill in bills:
        if bill == 5:
            fives += 1
        elif bill == 10:
            if fives == 0:
                return False
            fives -= 1
            tens += 1
        elif tens > 0 and fives > 0:
            # a 10 can only ever change a 20, so spend it before the fives
            tens -= 1
            fives -= 1
        elif fives >= 3:
            fives -= 3
        else:
            return False
    return True


if __name__ == "__main__":
    assert can_make_change([5, 5, 10, 5, 20]) is True
    assert can_make_change([5, 10, 10, 20]) is False
    assert can_make_change([]) is True
    assert can_make_change([10]) is False
    assert can_make_change([5, 5, 20]) is False
    assert can_make_change([5, 5, 5, 20]) is True
    assert can_make_change([5, 5, 5, 5, 10, 20, 10]) is True
    print("183 ok")
