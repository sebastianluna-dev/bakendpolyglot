# 118 · Flatten a Tree Into a List
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def flatten(root: TreeNode | None) -> None:
    node = root
    while node is not None:
        if node.left is not None:
            tail = node.left  # the last node of the left subtree in preorder
            while tail.right is not None:
                tail = tail.right
            tail.right = node.right
            node.right = node.left
            node.left = None
        node = node.right


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


def flattened(values: list[int | None]) -> list[int | None]:
    root = from_level_order(values)
    flatten(root)
    return to_level_order(root)


if __name__ == "__main__":
    assert flattened([10, 5, 20, 3, 7, None, 25]) == [10, None, 5, None, 3, None, 7, None, 20, None, 25]
    assert flattened([1, 2, None, 3]) == [1, None, 2, None, 3]
    assert flattened([]) == []
    assert flattened([8]) == [8]
    assert flattened([1, None, 2]) == [1, None, 2]
    assert flattened([1, 2, 3, None, None, 4]) == [1, None, 2, None, 3, None, 4]
    print("118 ok")
