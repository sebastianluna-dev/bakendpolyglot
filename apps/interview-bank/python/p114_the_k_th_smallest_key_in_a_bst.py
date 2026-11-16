# 114 · The k-th Smallest Key in a BST
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def kth_smallest(root: TreeNode | None, k: int) -> int:
    stack: list[TreeNode] = []
    node = root
    while True:
        while node is not None:
            stack.append(node)
            node = node.left
        smallest = stack.pop()  # the smallest key not yet counted
        k -= 1
        if k == 0:
            return smallest.val
        node = smallest.right


def from_level_order(values: list[int | None]) -> TreeNode | None:
    if not values or values[0] is None:
        return None
    root = TreeNode(values[0])
    queue = deque([root])
    for i in range(1, len(values), 2):
        node = queue.popleft()
        left = values[i]
        right = values[i + 1] if i + 1 < len(values) else None
        if left is not None:
            node.left = TreeNode(left)
            queue.append(node.left)
        if right is not None:
            node.right = TreeNode(right)
            queue.append(node.right)
    return root


if __name__ == "__main__":
    assert kth_smallest(from_level_order([20, 10, 30, 5, 15, 25, 40]), 3) == 15
    assert kth_smallest(from_level_order([6, 3, 8, 1, 4, None, 9, None, 2]), 2) == 2
    assert kth_smallest(from_level_order([6, 3, 8, 1, 4, None, 9, None, 2]), 7) == 9
    assert kth_smallest(from_level_order([42]), 1) == 42
    assert kth_smallest(from_level_order([1, None, 2, None, 3]), 3) == 3
    assert kth_smallest(from_level_order([3, 2, None, 1]), 1) == 1
    print("114 ok")
