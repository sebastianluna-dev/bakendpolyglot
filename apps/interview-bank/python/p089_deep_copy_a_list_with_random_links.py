# 089 · Deep-Copy a List With Random Links


class ListNode:
    def __init__(self, val: int, next: "ListNode | None" = None) -> None:
        self.val = val
        self.next = next
        self.random: ListNode | None = None


def deep_copy(head: ListNode | None) -> ListNode | None:
    copy_of: dict[ListNode, ListNode] = {}  # original node -> its copy
    node = head
    while node:
        copy_of[node] = ListNode(node.val)
        node = node.next
    node = head
    while node:
        copy_of[node].next = copy_of.get(node.next)  # get(None) is None
        copy_of[node].random = copy_of.get(node.random)
        node = node.next
    return copy_of.get(head)


def from_pairs(pairs: list[tuple[int, int]]) -> ListNode | None:
    """Each pair is (value, index of the random target), with -1 for no target."""
    nodes = [ListNode(val) for val, _ in pairs]
    for i, (_, target) in enumerate(pairs):
        nodes[i].next = nodes[i + 1] if i + 1 < len(nodes) else None
        if target >= 0:
            nodes[i].random = nodes[target]
    return nodes[0] if nodes else None


def to_pairs(head: ListNode | None) -> list[tuple[int, int]]:
    nodes = []
    while head:
        nodes.append(head)
        head = head.next
    index = {node: i for i, node in enumerate(nodes)}
    return [(n.val, index[n.random] if n.random else -1) for n in nodes]


def shares_node(a: ListNode | None, b: ListNode | None) -> bool:
    seen = set()
    while a:
        seen.add(a)
        a = a.next
    while b:
        if b in seen or b.random in seen:
            return True
        b = b.next
    return False


def check_copy(pairs: list[tuple[int, int]]) -> None:
    original = from_pairs(pairs)
    copy = deep_copy(original)
    assert to_pairs(copy) == pairs
    assert to_pairs(original) == pairs
    assert not shares_node(original, copy)


if __name__ == "__main__":
    check_copy([(4, 2), (9, 0), (6, -1)])
    check_copy([(1, 1), (2, 1)])
    check_copy([])
    check_copy([(5, 0)])
    check_copy([(7, 1), (7, 0)])
    check_copy([(3, -1), (8, -1), (2, -1)])
    print("089 ok")
