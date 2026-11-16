# 112 · What You See From the Right
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def right_side_view(root: TreeNode | None) -> list[int]:
    view: list[int] = []

    def visit(node: TreeNode | None, depth: int) -> None:
        if node is None:
            return
        if depth == len(view):
            view.append(node.val)  # first node reached at this depth
        visit(node.right, depth + 1)
        visit(node.left, depth + 1)

    visit(root, 0)
    return view


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
    assert right_side_view(from_level_order([10, 4, 15, 2, 6, None, 20, None, None, 5])) == [10, 15, 20, 5]
    assert right_side_view(from_level_order([1, 2])) == [1, 2]
    assert right_side_view(from_level_order([])) == []
    assert right_side_view(from_level_order([4])) == [4]
    assert right_side_view(from_level_order([1, 2, None, 3])) == [1, 2, 3]
    assert right_side_view(from_level_order([1, 2, 3, 4, 5, 6, 7])) == [1, 3, 7]
    print("112 ok")
