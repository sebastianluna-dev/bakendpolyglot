# 108 · Longest Path Between Two Nodes
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def longest_path(root: TreeNode | None) -> int:
    best = 0

    # Returns the depth in nodes, which is also the number of edges from the parent down.
    def depth(node: TreeNode | None) -> int:
        nonlocal best
        if node is None:
            return 0
        left, right = depth(node.left), depth(node.right)
        best = max(best, left + right)
        return 1 + max(left, right)

    depth(root)
    return best


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
    assert longest_path(from_level_order([8, 4, 11, 2, 6, None, None, 1])) == 4
    assert longest_path(from_level_order([1, 2, None, 3, 4, 5, None, None, 6, 7, None, None, 8])) == 6
    assert longest_path(from_level_order([])) == 0
    assert longest_path(from_level_order([9])) == 0
    assert longest_path(from_level_order([1, 2])) == 1
    print("108 ok")
