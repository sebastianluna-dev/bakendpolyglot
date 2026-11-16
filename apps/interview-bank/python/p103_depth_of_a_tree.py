# 103 · Depth of a Tree
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def max_depth(root: TreeNode | None) -> int:
    if root is None:
        return 0
    return 1 + max(max_depth(root.left), max_depth(root.right))


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
    assert max_depth(from_level_order([5, 2, 9, None, 4, 7, None, None, None, 6])) == 4
    assert max_depth(from_level_order([1, None, 2, None, 3])) == 3
    assert max_depth(from_level_order([])) == 0
    assert max_depth(from_level_order([42])) == 1
    assert max_depth(from_level_order([1, 2, 3])) == 2
    print("103 ok")
