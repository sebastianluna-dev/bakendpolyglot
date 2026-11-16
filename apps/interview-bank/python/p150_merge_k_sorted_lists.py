# 150 · Merge k Sorted Lists
import heapq


class ListNode:
    def __init__(self, val: int, next: "ListNode | None" = None) -> None:
        self.val = val
        self.next = next


def merge_lists(lists: list[ListNode | None]) -> ListNode | None:
    # (value, list index, node): the index breaks ties, so two nodes are never compared
    heap = [(head.val, i, head) for i, head in enumerate(lists) if head]
    heapq.heapify(heap)
    dummy = ListNode(0)
    tail = dummy
    while heap:
        _, i, node = heapq.heappop(heap)
        tail.next = node
        tail = node
        if node.next:
            heapq.heappush(heap, (node.next.val, i, node.next))
    return dummy.next


def merge(lists: list[list[int]]) -> list[int]:
    return to_list(merge_lists([from_list(values) for values in lists]))


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
    assert merge([[1, 4, 7], [2, 5], [0, 6, 9]]) == [0, 1, 2, 4, 5, 6, 7, 9]
    assert merge([[], [3], []]) == [3]
    assert merge([]) == []
    assert merge([[]]) == []
    assert merge([[2, 2], [2], [1, 2]]) == [1, 2, 2, 2, 2]
    assert merge([[-5, 0, 5]]) == [-5, 0, 5]
    assert merge([[10], [-10], [0], [-20, 20]]) == [-20, -10, 0, 10, 20]
    print("150 ok")
