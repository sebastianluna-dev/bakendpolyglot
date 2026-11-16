# 117 · Nodes No Smaller Than Their Ancestors
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def count_good_nodes(root: TreeNode | None) -> int:
    return 0 if root is None else count_from(root, root.val)


# highest is the largest value on the path from the root down to node's parent
def count_from(node: TreeNode | None, highest: int) -> int:
    if node is None:
        return 0
    good = 1 if node.val >= highest else 0
    next_highest = max(highest, node.val)
    return good + count_from(node.left, next_highest) + count_from(node.right, next_highest)


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
    assert count_good_nodes(from_level_order([5, 3, 8, 6, 2, 5, 9])) == 4
    assert count_good_nodes(from_level_order([2, 2, 2])) == 3
    assert count_good_nodes(from_level_order([])) == 0
    assert count_good_nodes(from_level_order([7])) == 1
    assert count_good_nodes(from_level_order([9, 8, None, 7])) == 1
    assert count_good_nodes(from_level_order([-5, -10, -1])) == 2
    print("117 ok")
