# 200 · Consistent Hash Ring

MASK = 0xFFFFFFFF  # Python ints never overflow, so keep 32 bits by hand


def fnv1a(s: str) -> int:
    h = 0x811C9DC5
    for b in s.encode("utf-8"):
        h ^= b
        h = (h * 0x01000193) & MASK
    return h


def mix32(h: int) -> int:
    h ^= h >> 16
    h = (h * 0x85EBCA6B) & MASK
    h ^= h >> 13
    h = (h * 0xC2B2AE35) & MASK
    h ^= h >> 16
    return h


def ring_hash(s: str) -> int:
    return mix32(fnv1a(s))


class HashRing:
    def __init__(self, replicas: int) -> None:
        self.replicas = replicas
        self.nodes: set[str] = set()
        self.points: list[tuple[int, str]] = []  # (hash, node), sorted by hash, then node

    def add_node(self, node: str) -> None:
        if node in self.nodes:
            return
        self.nodes.add(node)
        for i in range(self.replicas):
            self.points.append((ring_hash(f"{node}#{i}"), node))
        self.points.sort()

    def remove_node(self, node: str) -> None:
        if node not in self.nodes:
            return
        self.nodes.remove(node)
        self.points = [p for p in self.points if p[1] != node]

    def node_for(self, key: str) -> str:
        if not self.points:
            return ""
        h = ring_hash(key)
        lo, hi = 0, len(self.points)  # looking for the first point with hash >= h
        while lo < hi:
            mid = lo + (hi - lo) // 2
            if self.points[mid][0] >= h:
                hi = mid
            else:
                lo = mid + 1
        return self.points[lo % len(self.points)][1]  # past the last point: wrap to the first


KEYS = ["book:1", "book:2", "book:3", "book:4", "book:5", "book:6", "book:7", "book:37"]


def owners(ring: HashRing) -> str:
    return "".join(ring.node_for(k)[-1:] for k in KEYS)  # the last letter of each key's node


if __name__ == "__main__":
    assert fnv1a("") == 0x811C9DC5
    assert fnv1a("a") == 0xE40C292C
    assert fnv1a("foobar") == 0xBF9CF968
    assert mix32(0) == 0
    assert ring_hash("cache-a#0") == 0x6901E554
    assert ring_hash("book:37") == 0xFCFA1E45

    ring = HashRing(3)
    assert ring.node_for("book:1") == ""
    for name in ["cache-a", "cache-b", "cache-c"]:
        ring.add_node(name)
    assert owners(ring) == "babccccc"
    ring.add_node("cache-a")
    ring.remove_node("cache-z")
    assert owners(ring) == "babccccc"
    ring.remove_node("cache-b")
    assert owners(ring) == "aacccccc"  # only cache-b's keys moved
    ring.add_node("cache-b")
    assert owners(ring) == "babccccc"
    ring.add_node("cache-d")
    assert owners(ring) == "babdccdc"  # keys move only to the new node
    for name in ["cache-a", "cache-b", "cache-c", "cache-d"]:
        ring.remove_node(name)
    assert ring.node_for("book:1") == ""
    print("200 ok")
