# 196 · Token-Bucket Rate Limiter


class TokenBucket:
    def __init__(self, capacity: int, refill_per_second: int) -> None:
        self.capacity = capacity * 1000  # in thousandths of a token, so the arithmetic stays exact
        self.refill_per_second = refill_per_second
        self.tokens = self.capacity
        self.last_ms = 0

    def allow(self, now_ms: int) -> bool:
        # one token per second is one thousandth per millisecond
        self.tokens = min(self.capacity, self.tokens + (now_ms - self.last_ms) * self.refill_per_second)
        self.last_ms = now_ms
        if self.tokens < 1000:
            return False
        self.tokens -= 1000
        return True


def allow_all(bucket: TokenBucket, times: list[int]) -> list[bool]:
    return [bucket.allow(t) for t in times]


if __name__ == "__main__":
    assert allow_all(TokenBucket(3, 1), [0, 0, 0, 0, 999, 1000, 1000, 5000, 5000, 5000, 5000]) == [
        True, True, True, False, False, True, False, True, True, True, False,
    ]
    assert allow_all(TokenBucket(2, 4), [100, 100, 200, 350, 400, 600]) == [
        True, True, False, True, False, True,
    ]
    assert allow_all(TokenBucket(1, 1), [1_000_000_000, 1_000_000_000, 1_000_000_999, 1_000_001_000]) == [
        True, False, False, True,
    ]
    assert allow_all(TokenBucket(5, 10_000), [7, 7, 7, 7, 7, 7, 7]) == [True] * 5 + [False] * 2
    print("196 ok")
