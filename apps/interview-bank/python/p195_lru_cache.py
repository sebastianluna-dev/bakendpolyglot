# 195 · LRU Cache


class Node:
    __slots__ = ("key", "value", "prev", "next")

    def __init__(self, key: int, value: int) -> None:
        self.key = key
        self.value = value
        self.prev: Node = self
        self.next: Node = self


class LruCache:
    def __init__(self, capacity: int) -> None:
        self.capacity = capacity
        self.nodes: dict[int, Node] = {}
        self.head = Node(0, 0)  # sentinel: head.next is the most recent, head.prev the least recent

    def _unlink(self, node: Node) -> None:
        node.prev.next = node.next
        node.next.prev = node.prev

    def _push_front(self, node: Node) -> None:
        node.prev = self.head
        node.next = self.head.next
        self.head.next.prev = node
        self.head.next = node

    def get(self, key: int) -> int:
        node = self.nodes.get(key)
        if node is None:
            return -1
        self._unlink(node)
        self._push_front(node)
        return node.value

    def put(self, key: int, value: int) -> None:
        node = self.nodes.get(key)
        if node is not None:
            node.value = value
            self._unlink(node)
            self._push_front(node)
            return
        if len(self.nodes) == self.capacity:
            oldest = self.head.prev
            self._unlink(oldest)
            del self.nodes[oldest.key]
        node = Node(key, value)
        self.nodes[key] = node
        self._push_front(node)


if __name__ == "__main__":
    c = LruCache(2)
    c.put(1, 10)
    c.put(2, 20)
    assert c.get(1) == 10
    c.put(3, 30)  # evicts 2
    assert c.get(2) == -1
    assert c.get(3) == 30
    c.put(1, 11)  # an update counts as a use
    c.put(4, 40)  # evicts 3
    assert c.get(3) == -1
    assert c.get(1) == 11
    assert c.get(4) == 40

    one = LruCache(1)
    one.put(5, 50)
    assert one.get(5) == 50
    one.put(6, 60)
    assert one.get(5) == -1
    assert one.get(6) == 60
    assert one.get(7) == -1
    one.put(6, 61)
    assert one.get(6) == 61
    print("195 ok")
