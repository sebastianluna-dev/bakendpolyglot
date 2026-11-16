# 085 · Drop Repeated Values From a Sorted List


class ListNode:
    def __init__(self, val: int, next: "ListNode | None" = None) -> None:
        self.val = val
        self.next = next


def drop_repeats(head: ListNode | None) -> ListNode | None:
    node = head
    while node and node.next:
        if node.next.val == node.val:
            node.next = node.next.next  # stay: the new next may repeat too
        else:
            node = node.next
    return head


def drop(values: list[int]) -> list[int]:
    return to_list(drop_repeats(from_list(values)))


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
    assert drop([1, 1, 2, 3, 3, 3]) == [1, 2, 3]
    assert drop([4, 4, 4]) == [4]
    assert drop([]) == []
    assert drop([1, 2, 3]) == [1, 2, 3]
    assert drop([-2, -2, 0, 5, 5]) == [-2, 0, 5]
    assert drop([8]) == [8]
    print("085 ok")
