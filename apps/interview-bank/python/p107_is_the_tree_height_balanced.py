# 107 · Is the Tree Height-Balanced?
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def is_balanced(root: TreeNode | None) -> bool:
    return balanced_height(root) != -1


# The height of the subtree, or -1 as soon as any subtree in it is unbalanced.
def balanced_height(node: TreeNode | None) -> int:
    if node is None:
        return 0
    left = balanced_height(node.left)
    if left == -1:
        return -1
    right = balanced_height(node.right)
    if right == -1 or abs(left - right) > 1:
        return -1
    return 1 + max(left, right)


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
    assert is_balanced(from_level_order([10, 5, 15, None, None, 12, 20]))
    assert not is_balanced(from_level_order([10, 5, None, 3, None, 1]))
    assert is_balanced(from_level_order([]))
    assert not is_balanced(from_level_order([1, 2, 3, 4, None, None, 5, 6, None, None, 7]))
    assert is_balanced(from_level_order([1, 2, 3, 4]))
    assert is_balanced(from_level_order([1, 2]))
    print("107 ok")
