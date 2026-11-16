# 082 · Merge Two Sorted Lists


class ListNode:
    def __init__(self, val: int, next: "ListNode | None" = None) -> None:
        self.val = val
        self.next = next


def merge_sorted(a: ListNode | None, b: ListNode | None) -> ListNode | None:
    dummy = ListNode(0)
    tail = dummy
    while a and b:
        if a.val <= b.val:
            tail.next = a
            tail = a
            a = a.next
        else:
            tail.next = b
            tail = b
            b = b.next
    tail.next = a or b  # whatever is left is already sorted
    return dummy.next


def merge(a: list[int], b: list[int]) -> list[int]:
    return to_list(merge_sorted(from_list(a), from_list(b)))


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
    assert merge([1, 4, 6], [2, 3, 6, 9]) == [1, 2, 3, 4, 6, 6, 9]
    assert merge([], [5, 8]) == [5, 8]
    assert merge([], []) == []
    assert merge([7, 8], [1, 2]) == [1, 2, 7, 8]
    assert merge([-3, 0], [-3]) == [-3, -3, 0]
    first, second = from_list([2]), from_list([2])
    merged = merge_sorted(first, second)
    assert merged is first and merged.next is second
    print("082 ok")
