# 106 · Is the Tree Symmetric?
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def is_symmetric(root: TreeNode | None) -> bool:
    return root is None or is_mirror(root.left, root.right)


def is_mirror(a: TreeNode | None, b: TreeNode | None) -> bool:
    if a is None or b is None:
        return a is b
    return a.val == b.val and is_mirror(a.left, b.right) and is_mirror(a.right, b.left)


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
    assert is_symmetric(from_level_order([7, 4, 4, 1, 9, 9, 1]))
    assert not is_symmetric(from_level_order([5, 3, 3, 8, None, None, 2]))
    assert is_symmetric(from_level_order([]))
    assert is_symmetric(from_level_order([1]))
    assert not is_symmetric(from_level_order([2, 3, 3, None, 4, None, 4]))
    assert not is_symmetric(from_level_order([1, 2, 3]))
    print("106 ok")
