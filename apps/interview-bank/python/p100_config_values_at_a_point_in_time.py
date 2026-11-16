# 100 · Config Values at a Point in Time


class ConfigHistory:
    def __init__(self) -> None:
        self.history: dict[str, list[tuple[int, str]]] = {}  # key -> (timestamp, value), oldest first

    def set(self, key: str, value: str, timestamp: int) -> None:
        # timestamps only grow, so the list stays sorted
        self.history.setdefault(key, []).append((timestamp, value))

    def get(self, key: str, timestamp: int) -> str:
        entries = self.history.get(key, [])
        lo, hi = 0, len(entries)  # looking for the first entry set after timestamp
        while lo < hi:
            mid = lo + (hi - lo) // 2
            if entries[mid][0] > timestamp:
                hi = mid
            else:
                lo = mid + 1
        return "" if lo == 0 else entries[lo - 1][1]


if __name__ == "__main__":
    h = ConfigHistory()
    assert h.get("hold.minutes", 100) == ""
    h.set("hold.minutes", "10", 100)
    h.set("hold.minutes", "15", 250)
    h.set("currency", "EUR", 120)
    assert h.get("hold.minutes", 99) == ""
    assert h.get("hold.minutes", 100) == "10"
    assert h.get("hold.minutes", 249) == "10"
    assert h.get("hold.minutes", 250) == "15"
    assert h.get("hold.minutes", 1000) == "15"
    assert h.get("currency", 119) == ""
    assert h.get("currency", 500) == "EUR"
    assert h.get("refunds", 500) == ""
    h.set("hold.minutes", "20", 400)
    assert h.get("hold.minutes", 399) == "15"
    assert h.get("hold.minutes", 400) == "20"
    print("100 ok")
