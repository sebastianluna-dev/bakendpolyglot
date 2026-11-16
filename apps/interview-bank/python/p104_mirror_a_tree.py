# 104 · Mirror a Tree
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def mirror_tree(root: TreeNode | None) -> TreeNode | None:
    if root is None:
        return None
    root.left, root.right = mirror_tree(root.right), mirror_tree(root.left)
    return root


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


def to_level_order(root: TreeNode | None) -> list[int | None]:
    if root is None:
        return []
    values: list[int | None] = [root.val]
    queue = deque([root])
    while queue:
        node = queue.popleft()
        for child in (node.left, node.right):
            if child is None:
                values.append(None)
            else:
                values.append(child.val)
                queue.append(child)
    while values[-1] is None:
        values.pop()
    return values


if __name__ == "__main__":
    assert to_level_order(mirror_tree(from_level_order([6, 2, 8, 1, 4, None, 9]))) == [6, 8, 2, 9, None, 4, 1]
    assert to_level_order(mirror_tree(from_level_order([3, 1]))) == [3, None, 1]
    assert to_level_order(mirror_tree(from_level_order([]))) == []
    assert to_level_order(mirror_tree(from_level_order([7]))) == [7]
    assert to_level_order(mirror_tree(from_level_order([1, 2, None, 3]))) == [1, None, 2, None, 3]
    print("104 ok")
