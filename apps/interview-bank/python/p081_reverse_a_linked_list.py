# 081 · Reverse a Linked List


class ListNode:
    def __init__(self, val: int, next: "ListNode | None" = None) -> None:
        self.val = val
        self.next = next


def reverse_list(head: ListNode | None) -> ListNode | None:
    prev: ListNode | None = None
    node = head
    while node:
        nxt = node.next  # save it before the link is overwritten
        node.next = prev
        prev = node
        node = nxt
    return prev


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
    assert to_list(reverse_list(from_list([4, 8, 15, 16, 23]))) == [23, 16, 15, 8, 4]
    assert to_list(reverse_list(from_list([2, 9]))) == [9, 2]
    assert to_list(reverse_list(from_list([7]))) == [7]
    assert reverse_list(from_list([])) is None
    assert to_list(reverse_list(from_list([5, 5, 1]))) == [1, 5, 5]
    print("081 ok")
