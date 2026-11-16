# 111 · Level-by-Level Traversal
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def level_order(root: TreeNode | None) -> list[list[int]]:
    levels: list[list[int]] = []
    level = [root] if root else []
    while level:
        levels.append([node.val for node in level])
        level = [child for node in level for child in (node.left, node.right) if child]
    return levels


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
    assert level_order(from_level_order([8, 4, 12, 2, None, 10, 14])) == [[8], [4, 12], [2, 10, 14]]
    assert level_order(from_level_order([5, 6, None, 7, 8])) == [[5], [6], [7, 8]]
    assert level_order(from_level_order([])) == []
    assert level_order(from_level_order([9])) == [[9]]
    assert level_order(from_level_order([1, 2, 3, None, 4, 5, None, None, None, 6])) == [[1], [2, 3], [4, 5], [6]]
    print("111 ok")
