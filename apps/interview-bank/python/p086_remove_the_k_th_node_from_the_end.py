# 086 · Remove the k-th Node From the End


class ListNode:
    def __init__(self, val: int, next: "ListNode | None" = None) -> None:
        self.val = val
        self.next = next


def remove_kth_from_end(head: ListNode | None, k: int) -> ListNode | None:
    dummy = ListNode(0, head)  # the node before the head, in case the head goes
    lead = head
    for _ in range(k):
        lead = lead.next
    trail = dummy
    while lead:
        lead = lead.next
        trail = trail.next
    trail.next = trail.next.next
    return dummy.next


def remove(values: list[int], k: int) -> list[int]:
    return to_list(remove_kth_from_end(from_list(values), k))


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
    assert remove([10, 20, 30, 40, 50], 2) == [10, 20, 30, 50]
    assert remove([8], 1) == []
    assert remove([1, 2], 2) == [2]
    assert remove([1, 2], 1) == [1]
    assert remove([5, 6, 7], 3) == [6, 7]
    assert remove([5, 6, 7], 1) == [5, 6]
    print("086 ok")
