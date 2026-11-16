# 092 · Where Would It Go?


def insert_position(deadlines: list[int], deadline: int) -> int:
    lo, hi = 0, len(deadlines)  # the answer may be len(deadlines) itself
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if deadlines[mid] >= deadline:
            hi = mid
        else:
            lo = mid + 1
    return lo


if __name__ == "__main__":
    stops = [480, 540, 600, 720]
    assert insert_position(stops, 610) == 3
    assert insert_position([480, 540, 540, 600], 540) == 1
    assert insert_position([], 300) == 0
    assert insert_position(stops, 100) == 0
    assert insert_position(stops, 800) == 4
    assert insert_position(stops, 480) == 0
    assert insert_position(stops, 720) == 3
    assert insert_position([5, 5, 5], 5) == 0
    print("092 ok")
