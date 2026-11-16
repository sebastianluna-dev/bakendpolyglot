# 083 · Does the List Loop?


class ListNode:
    def __init__(self, val: int, next: "ListNode | None" = None) -> None:
        self.val = val
        self.next = next


def has_loop(head: ListNode | None) -> bool:
    slow = fast = head
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
        if slow is fast:
            return True
    return False


def with_loop(values: list[int], loop_to: int) -> ListNode | None:
    """Builds the list and links its last node to the node at index loop_to (-1: no loop)."""
    nodes = [ListNode(v) for v in values]
    for a, b in zip(nodes, nodes[1:]):
        a.next = b
    if loop_to >= 0:
        nodes[-1].next = nodes[loop_to]
    return nodes[0] if nodes else None


if __name__ == "__main__":
    assert has_loop(with_loop([3, 8, 1, 6], 1)) is True
    assert has_loop(with_loop([4, 2], -1)) is False
    assert has_loop(with_loop([5], 0)) is True
    assert has_loop(with_loop([], -1)) is False
    assert has_loop(with_loop([2, 5, 2], -1)) is False
    assert has_loop(with_loop([1, 2, 3, 4, 5, 6], 0)) is True
    assert has_loop(with_loop([7, 1], 1)) is True
    print("083 ok")
