# 087 · Interleave a List With Its Reverse


class ListNode:
    def __init__(self, val: int, next: "ListNode | None" = None) -> None:
        self.val = val
        self.next = next


def interleave_with_reverse(head: ListNode | None) -> None:
    if head is None:
        return
    slow = fast = head
    while fast.next and fast.next.next:
        slow = slow.next
        fast = fast.next.next
    second = reverse(slow.next)
    slow.next = None  # cut the first half off
    first = head
    while first and second:
        next_first, next_second = first.next, second.next
        first.next = second
        second.next = next_first
        first, second = next_first, next_second


def reverse(head: ListNode | None) -> ListNode | None:
    prev: ListNode | None = None
    node = head
    while node:
        nxt = node.next
        node.next = prev
        prev = node
        node = nxt
    return prev


def interleaved(values: list[int]) -> list[int]:
    head = from_list(values)
    interleave_with_reverse(head)
    return to_list(head)


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
    assert interleaved([1, 2, 3, 4, 5, 6]) == [1, 6, 2, 5, 3, 4]
    assert interleaved([3, 8, 5, 2, 9]) == [3, 9, 8, 2, 5]
    assert interleaved([]) == []
    assert interleaved([7]) == [7]
    assert interleaved([1, 2]) == [1, 2]
    assert interleaved([10, 20, 30]) == [10, 30, 20]
    assert interleaved([1, 2, 3, 4]) == [1, 4, 2, 3]
    print("087 ok")
