# 192 · Keep the Latest Version of Each Record
from typing import NamedTuple


class Revision(NamedTuple):
    id: int
    version: int
    payload: str


def latest_versions(revisions: list[Revision]) -> list[Revision]:
    latest: dict[int, Revision] = {}
    for r in revisions:
        kept = latest.get(r.id)
        if kept is None or r.version > kept.version:  # strictly greater: the first of equal versions stays
            latest[r.id] = r
    return sorted(latest.values(), key=lambda r: r.id)


if __name__ == "__main__":
    R = Revision
    assert latest_versions([
        R(7, 1, "created"), R(3, 2, "in transit"), R(7, 3, "delivered"), R(3, 1, "created"), R(7, 2, "in transit"),
    ]) == [R(3, 2, "in transit"), R(7, 3, "delivered")]
    assert latest_versions([
        R(5, 4, "first"), R(5, 4, "replayed"), R(1, 1, "only"),
    ]) == [R(1, 1, "only"), R(5, 4, "first")]
    assert latest_versions([]) == []
    assert latest_versions([R(42, 9, "x")]) == [R(42, 9, "x")]
    assert latest_versions([
        R(10, 1, "a"), R(2, 1, "b"), R(10, 2, "c"), R(-4, 3, "d"), R(2, 5, "e"),
    ]) == [R(-4, 3, "d"), R(2, 5, "e"), R(10, 2, "c")]
    print("192 ok")
