# 110 · Sum of a Key Range in a BST
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def range_sum(root: TreeNode | None, lo: int, hi: int) -> int:
    if root is None:
        return 0
    if root.val < lo:
        return range_sum(root.right, lo, hi)  # the left subtree is even smaller
    if root.val > hi:
        return range_sum(root.left, lo, hi)
    return root.val + range_sum(root.left, lo, hi) + range_sum(root.right, lo, hi)


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
    assert range_sum(from_level_order([20, 10, 30, 5, 15, 25, 40]), 12, 27) == 60
    assert range_sum(from_level_order([8, 3, 12, None, 6]), 9, 11) == 0
    assert range_sum(from_level_order([]), 1, 100) == 0
    assert range_sum(from_level_order([7]), 7, 7) == 7
    assert range_sum(from_level_order([20, 10, 30, 5, 15, 25, 40]), 1, 100) == 145
    assert range_sum(from_level_order([20, 10, 30, 5, 15, 25, 40]), 10, 20) == 45
    print("110 ok")
