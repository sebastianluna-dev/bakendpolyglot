# 120 · Serialize and Deserialize a Tree
from collections import deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def serialize(root: TreeNode | None) -> str:
    tokens: list[str] = []

    def write(node: TreeNode | None) -> None:
        if node is None:
            tokens.append("#")
            return
        tokens.append(str(node.val))
        write(node.left)
        write(node.right)

    write(root)
    return ",".join(tokens)


def deserialize(data: str) -> TreeNode | None:
    tokens = iter(data.split(","))

    def read() -> TreeNode | None:
        token = next(tokens)
        if token == "#":
            return None
        node = TreeNode(int(token))
        node.left = read()
        node.right = read()
        return node

    return read()


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
    assert serialize(from_level_order([7, 3, 9, None, 4, 8])) == "7,3,#,4,#,#,9,8,#,#,#"
    assert serialize(from_level_order([])) == "#"
    assert to_level_order(deserialize("5,-2,#,#,#")) == [5, -2]
    trees: list[list[int | None]] = [[7, 3, 9, None, 4, 8], [], [-1000], [1, None, 2, None, 3], [10, -20, 30, -40, 50, -60, 70]]
    for tree in trees:
        assert to_level_order(deserialize(serialize(from_level_order(tree)))) == tree
    print("120 ok")
