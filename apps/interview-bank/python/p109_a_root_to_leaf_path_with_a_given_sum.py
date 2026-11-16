# 109 · A Root-to-Leaf Path With a Given Sum
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def has_path_sum(root: TreeNode | None, target: int) -> bool:
    if root is None:
        return False
    if root.left is None and root.right is None:
        return root.val == target
    rest = target - root.val
    return has_path_sum(root.left, rest) or has_path_sum(root.right, rest)


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
    assert has_path_sum(from_level_order([5, 3, 8, 1, None, None, 4]), 9)
    assert not has_path_sum(from_level_order([2, 7]), 2)
    assert not has_path_sum(from_level_order([]), 0)
    assert has_path_sum(from_level_order([5, 3, 8, 1, None, None, 4]), 17)
    assert not has_path_sum(from_level_order([5, 3, 8, 1, None, None, 4]), 8)
    assert has_path_sum(from_level_order([-3, 1, -2]), -5)
    print("109 ok")
