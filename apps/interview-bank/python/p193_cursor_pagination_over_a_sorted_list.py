# 193 · Cursor Pagination Over a Sorted List
from typing import NamedTuple


class Page(NamedTuple):
    items: list[int]
    next: int | None


def fetch_page(ids: list[int], after: int | None, limit: int) -> Page:
    start = 0
    if after is not None:
        lo, hi = 0, len(ids)  # looking for the first id greater than after
        while lo < hi:
            mid = lo + (hi - lo) // 2
            if ids[mid] > after:
                hi = mid
            else:
                lo = mid + 1
        start = lo
    end = min(start + limit, len(ids))
    items = ids[start:end]
    return Page(items, items[-1] if end < len(ids) else None)


if __name__ == "__main__":
    ids = [3, 8, 15, 21, 30, 42, 57]
    assert fetch_page(ids, None, 3) == Page([3, 8, 15], 15)
    assert fetch_page(ids, 15, 3) == Page([21, 30, 42], 42)
    assert fetch_page(ids, 42, 3) == Page([57], None)
    assert fetch_page(ids, 20, 3) == Page([21, 30, 42], 42)
    assert fetch_page(ids, 57, 3) == Page([], None)
    assert fetch_page(ids, 1, 10) == Page([3, 8, 15, 21, 30, 42, 57], None)
    assert fetch_page([3, 8, 15, 21, 30, 42], 15, 3) == Page([21, 30, 42], None)
    assert fetch_page([], None, 5) == Page([], None)
    print("193 ok")
