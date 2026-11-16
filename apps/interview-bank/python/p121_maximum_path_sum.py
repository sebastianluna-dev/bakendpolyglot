# 121 · Maximum Path Sum
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def max_path_sum(root: TreeNode) -> int:
    best = root.val

    # returns the best sum of a path that starts at node and goes down, or 0 for no node
    def gain(node: TreeNode | None) -> int:
        nonlocal best
        if node is None:
            return 0
        left = max(0, gain(node.left))  # a negative branch is better left out
        right = max(0, gain(node.right))
        best = max(best, node.val + left + right)
        return node.val + max(left, right)

    gain(root)
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
    assert max_path_sum(from_level_order([6, -8, 3, None, 9, -1, 4])) == 14
    assert max_path_sum(from_level_order([-3, -1, -2])) == -1
    assert max_path_sum(from_level_order([-5])) == -5
    assert max_path_sum(from_level_order([-10, 4, -1, 6, 7])) == 17
    assert max_path_sum(from_level_order([2, -1, None, 5])) == 6
    assert max_path_sum(from_level_order([1, -2, 3, 4, 5, -6, 2])) == 9
    print("121 ok")
