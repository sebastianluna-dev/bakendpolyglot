# 088 · Add Two Numbers Stored as Lists


class ListNode:
    def __init__(self, val: int, next: "ListNode | None" = None) -> None:
        self.val = val
        self.next = next


def add_lists(a: ListNode | None, b: ListNode | None) -> ListNode | None:
    dummy = ListNode(0)
    tail = dummy
    carry = 0
    while a or b or carry:
        total = carry
        if a:
            total += a.val
            a = a.next
        if b:
            total += b.val
            b = b.next
        carry, digit = divmod(total, 10)
        tail.next = ListNode(digit)
        tail = tail.next
    return dummy.next


def add(a: list[int], b: list[int]) -> list[int]:
    return to_list(add_lists(from_list(a), from_list(b)))


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
    assert add([5, 2, 9], [8, 4]) == [3, 7, 9]
    assert add([9, 9, 9], [1]) == [0, 0, 0, 1]
    assert add([0], [0]) == [0]
    assert add([1], [6, 0, 3]) == [7, 0, 3]
    assert add([9, 9], [9, 9]) == [8, 9, 1]
    assert add([9] * 20, [1]) == [0] * 20 + [1]
    print("088 ok")
