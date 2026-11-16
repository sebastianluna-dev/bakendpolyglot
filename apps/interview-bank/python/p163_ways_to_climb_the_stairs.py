# 163 · Ways to Climb the Stairs


def ways_to_climb(n: int) -> int:
    prev, cur = 1, 1  # ways to reach steps i - 2 and i - 1
    for _ in range(2, n + 1):
        prev, cur = cur, prev + cur
    return cur


if __name__ == "__main__":
    assert ways_to_climb(4) == 5
    assert ways_to_climb(1) == 1
    assert ways_to_climb(0) == 1
    assert ways_to_climb(2) == 2
    assert ways_to_climb(10) == 89
    assert ways_to_climb(46) == 2971215073
    assert ways_to_climb(70) == 308061521170129
    print("163 ok")
