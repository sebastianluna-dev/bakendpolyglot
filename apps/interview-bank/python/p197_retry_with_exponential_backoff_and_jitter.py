# 197 · Retry With Exponential Backoff and Jitter
from collections.abc import Callable


def retry[T](
    operation: Callable[[], T],
    max_attempts: int,
    base_delay_ms: int,
    cap_ms: int,
    sleep: Callable[[int], None],
    random: Callable[[], float],
) -> T:
    ceiling = base_delay_ms
    for _ in range(max_attempts - 1):
        try:
            return operation()
        except Exception:
            sleep(int(random() * ceiling))  # full jitter: anywhere in [0, ceiling)
            ceiling = min(cap_ms, ceiling * 2)
    return operation()  # the last attempt: its exception reaches the caller


def flaky(failures: int, result: str) -> Callable[[], str]:
    calls = 0

    def operation() -> str:
        nonlocal calls
        calls += 1
        if calls <= failures:
            raise RuntimeError(f"timeout {calls}")
        return result

    return operation


def cycle(values: list[float]) -> Callable[[], float]:
    calls = 0

    def random() -> float:
        nonlocal calls
        calls += 1
        return values[(calls - 1) % len(values)]

    return random


def failure(op: Callable[[], str]) -> str:
    try:
        op()
    except RuntimeError as e:
        return str(e)
    return "no error"


if __name__ == "__main__":
    delays: list[int] = []
    assert retry(flaky(2, "receipt-42"), 5, 100, 1000, delays.append, cycle([0.5])) == "receipt-42"
    assert delays == [50, 100]

    delays = []
    op = flaky(10, "never")
    assert failure(lambda: retry(op, 4, 100, 250, delays.append, cycle([0.5, 0.25, 0.75]))) == "timeout 4"
    assert delays == [50, 50, 187]

    delays = []
    assert retry(flaky(0, "first try"), 3, 100, 1000, delays.append, cycle([0.5])) == "first try"
    assert delays == []

    delays = []
    assert failure(lambda: retry(flaky(1, "late"), 1, 100, 1000, delays.append, cycle([0.5]))) == "timeout 1"
    assert delays == []

    delays = []
    assert retry(flaky(6, "ok"), 7, 1000, 5000, delays.append, cycle([0.999])) == "ok"
    assert delays == [999, 1998, 3996, 4995, 4995, 4995]
    print("197 ok")
