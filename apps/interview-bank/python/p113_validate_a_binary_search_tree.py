# 113 · Validate a Binary Search Tree
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def is_search_tree(root: TreeNode | None) -> bool:
    return within(root, None, None)


# lo and hi are the nearest ancestors the key must stay above and below; None means no bound
def within(node: TreeNode | None, lo: TreeNode | None, hi: TreeNode | None) -> bool:
    if node is None:
        return True
    if lo is not None and node.val <= lo.val:
        return False
    if hi is not None and node.val >= hi.val:
        return False
    return within(node.left, lo, node) and within(node.right, node, hi)


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
    MIN, MAX = -(2**31), 2**31 - 1
    assert is_search_tree(from_level_order([8, 4, 12, 2, 6, 10, 14]))
    assert not is_search_tree(from_level_order([8, 4, 12, 2, 9, 10, 14]))
    assert is_search_tree(from_level_order([]))
    assert not is_search_tree(from_level_order([5, 5]))
    assert is_search_tree(from_level_order([MIN, None, MAX]))
    assert not is_search_tree(from_level_order([MAX, MAX]))
    assert not is_search_tree(from_level_order([MIN, MIN]))
    print("113 ok")
