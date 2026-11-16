# 105 · Are Two Trees Identical?
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def are_identical(a: TreeNode | None, b: TreeNode | None) -> bool:
    if a is None or b is None:
        return a is b
    return a.val == b.val and are_identical(a.left, b.left) and are_identical(a.right, b.right)


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
    assert are_identical(from_level_order([4, 2, 6]), from_level_order([4, 2, 6]))
    assert not are_identical(from_level_order([4, 2]), from_level_order([4, None, 2]))
    assert are_identical(from_level_order([]), from_level_order([]))
    assert not are_identical(from_level_order([1]), from_level_order([]))
    assert not are_identical(from_level_order([1, 2, 3]), from_level_order([1, 2, 4]))
    assert are_identical(from_level_order([5, None, 8, 7]), from_level_order([5, None, 8, 7]))
    print("105 ok")
