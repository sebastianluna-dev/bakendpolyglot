# 116 · Rebuild a Tree From Two Traversals
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def build_tree(preorder: list[int], inorder: list[int]) -> TreeNode | None:
    position = {value: i for i, value in enumerate(inorder)}

    # builds the subtree whose preorder starts at pre and whose inorder is inorder[lo..hi]
    def build(pre: int, lo: int, hi: int) -> TreeNode | None:
        if lo > hi:
            return None
        root = TreeNode(preorder[pre])
        mid = position[root.val]
        root.left = build(pre + 1, lo, mid - 1)
        root.right = build(pre + 1 + (mid - lo), mid + 1, hi)
        return root

    return build(0, 0, len(inorder) - 1)


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
    assert to_level_order(build_tree([8, 4, 2, 6, 12, 14], [2, 4, 6, 8, 12, 14])) == [8, 4, 12, 2, 6, None, 14]
    assert to_level_order(build_tree([1, 2, 3], [3, 2, 1])) == [1, 2, None, 3]
    assert to_level_order(build_tree([], [])) == []
    assert to_level_order(build_tree([5], [5])) == [5]
    assert to_level_order(build_tree([1, 2, 3], [1, 2, 3])) == [1, None, 2, None, 3]
    assert to_level_order(build_tree([-1, -3, 0], [-3, -1, 0])) == [-1, -3, 0]
    print("116 ok")
