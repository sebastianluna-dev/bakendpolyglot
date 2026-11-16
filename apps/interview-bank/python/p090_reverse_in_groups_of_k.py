# 090 · Reverse in Groups of k


class ListNode:
    def __init__(self, val: int, next: "ListNode | None" = None) -> None:
        self.val = val
        self.next = next


def reverse_in_groups(head: ListNode | None, k: int) -> ListNode | None:
    dummy = ListNode(0, head)
    before = dummy  # the node just before the next group
    while True:
        last: ListNode | None = before
        for _ in range(k):
            if last is None:
                break
            last = last.next
        if last is None:
            return dummy.next  # fewer than k nodes left: keep them as they are
        after = last.next
        first = before.next
        prev, node = after, first
        while node is not after:
            nxt = node.next
            node.next = prev
            prev = node
            node = nxt
        before.next = last
        before = first


def groups(values: list[int], k: int) -> list[int]:
    return to_list(reverse_in_groups(from_list(values), k))


def from_list(values: list[int]) -> ListNode | None:
    head: ListNode | None = None
    for value in reversed(values):
        head = ListNode(value, head)
    return head


def to_list(head: ListNode | None) -> list[int]:
    values: list[int] = []
    while head:
        values.append(head.val)
        head = head.next
    return values


if __name__ == "__main__":
    assert groups([1, 2, 3, 4, 5, 6, 7, 8], 3) == [3, 2, 1, 6, 5, 4, 7, 8]
    assert groups([1, 2, 3, 4, 5, 6], 2) == [2, 1, 4, 3, 6, 5]
    assert groups([4, 5, 6], 1) == [4, 5, 6]
    assert groups([4, 5, 6], 3) == [6, 5, 4]
    assert groups([4, 5, 6], 4) == [4, 5, 6]
    assert groups([], 2) == []
    head = from_list([1, 2, 3])
    assert reverse_in_groups(head, 3).next.next is head
    print("090 ok")
