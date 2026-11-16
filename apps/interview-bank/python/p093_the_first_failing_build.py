# 093 · The First Failing Build
from collections.abc import Callable


def first_failing_build(n: int, is_bad: Callable[[int], bool]) -> int:
    lo, hi = 1, n + 1  # n + 1 stands for "no build fails"
    while lo < hi:
        mid = lo + (hi - lo) // 2
        if is_bad(mid):
            hi = mid
        else:
            lo = mid + 1
    return lo if lo <= n else -1


if __name__ == "__main__":
    calls = 0

    def failing_from(first: int) -> Callable[[int], bool]:
        def is_bad(build: int) -> bool:
            global calls
            calls += 1
            return build >= first

        return is_bad

    assert first_failing_build(5, failing_from(4)) == 4
    assert first_failing_build(3, failing_from(4)) == -1
    assert first_failing_build(1, failing_from(1)) == 1
    assert first_failing_build(1, failing_from(2)) == -1
    assert first_failing_build(5, failing_from(1)) == 1
    calls = 0
    assert first_failing_build(1000, failing_from(777)) == 777
    assert calls <= 10
    calls = 0
    assert first_failing_build(2_000_000_000, failing_from(1_999_999_999)) == 1_999_999_999
    assert calls <= 31
    print("093 ok")
