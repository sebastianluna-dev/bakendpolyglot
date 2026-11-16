# 199 · Bounded Worker Pool With Ordered Results
import threading
import time
from collections.abc import Callable
from concurrent.futures import ThreadPoolExecutor


def map_limit[T, R](items: list[T], limit: int, fn: Callable[[T], R]) -> list[R]:
    with ThreadPoolExecutor(max_workers=limit) as pool:  # the pool size is the limit
        return list(pool.map(fn, items))  # input order, whatever order the calls finish in


def measure(durations: list[int], limit: int) -> tuple[list[int], int]:
    lock = threading.Lock()
    running = 0
    peak = 0

    def task(ms: int) -> int:
        nonlocal running, peak
        with lock:
            running += 1
            peak = max(peak, running)
        time.sleep(ms / 1000)
        with lock:
            running -= 1
        return ms * 10

    results = map_limit(durations, limit, task)
    return results, peak


if __name__ == "__main__":
    durations = [40, 20, 30, 20, 25, 20, 30, 20]  # later tasks often finish before earlier ones
    assert measure(durations, 3) == ([400, 200, 300, 200, 250, 200, 300, 200], 3)
    assert measure([20, 20, 20], 1) == ([200, 200, 200], 1)
    assert measure([30, 20], 10) == ([300, 200], 2)
    assert measure([], 4) == ([], 0)
    print("199 ok")
