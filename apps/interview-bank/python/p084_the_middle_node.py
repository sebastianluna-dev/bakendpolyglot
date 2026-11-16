# 084 · The Middle Node


class ListNode:
    def __init__(self, val: int, next: "ListNode | None" = None) -> None:
        self.val = val
        self.next = next


def middle_node(head: ListNode | None) -> ListNode | None:
    slow = fast = head
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
    return slow


def from_middle(values: list[int]) -> list[int]:
    return to_list(middle_node(from_list(values)))


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
    assert from_middle([3, 1, 4, 1, 5]) == [4, 1, 5]
    assert from_middle([2, 7, 1, 8]) == [1, 8]
    assert from_middle([6]) == [6]
    assert middle_node(None) is None
    assert from_middle([9, 4]) == [4]
    head = from_list([10, 20, 30])
    assert middle_node(head) is head.next
    print("084 ok")
