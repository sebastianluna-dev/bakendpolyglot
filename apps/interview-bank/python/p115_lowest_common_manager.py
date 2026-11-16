# 115 · Lowest Common Manager
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def lowest_common_manager(root: TreeNode | None, a: int, b: int) -> int:
    return lowest(root, a, b).val  # never None: both ids are in the tree


# returns the answer if both ids are below node, else whichever of the two is below it, else None
def lowest(node: TreeNode | None, a: int, b: int) -> TreeNode | None:
    if node is None or node.val in (a, b):
        return node
    left = lowest(node.left, a, b)
    right = lowest(node.right, a, b)
    if left is not None and right is not None:
        return node
    return left if left is not None else right


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
    chart = [1, 2, 3, 4, 5, 6, 7, None, None, 8, 9]
    assert lowest_common_manager(from_level_order(chart), 8, 4) == 2
    assert lowest_common_manager(from_level_order(chart), 9, 7) == 1
    assert lowest_common_manager(from_level_order(chart), 5, 9) == 5
    assert lowest_common_manager(from_level_order(chart), 6, 6) == 6
    assert lowest_common_manager(from_level_order(chart), 8, 9) == 5
    assert lowest_common_manager(from_level_order([1, 2]), 2, 1) == 1
    print("115 ok")
