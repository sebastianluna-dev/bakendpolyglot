# 119 · Count Downward Paths With a Given Sum
from collections import defaultdict, deque


class TreeNode:
    def __init__(self, val: int, left: "TreeNode | None" = None, right: "TreeNode | None" = None) -> None:
        self.val = val
        self.left = left
        self.right = right


def count_paths_with_sum(root: TreeNode | None, target: int) -> int:
    seen: defaultdict[int, int] = defaultdict(int)  # prefix sums on the current root path -> count
    seen[0] = 1

    def visit(node: TreeNode | None, prefix: int) -> int:
        if node is None:
            return 0
        total = prefix + node.val
        count = seen[total - target]
        seen[total] += 1
        count += visit(node.left, total) + visit(node.right, total)
        seen[total] -= 1
        return count

    return visit(root, 0)


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
    assert count_paths_with_sum(from_level_order([4, 2, -1, 1, 3, None, 5]), 5) == 2
    assert count_paths_with_sum(from_level_order([1, -1, 1, 1]), 1) == 4
    assert count_paths_with_sum(from_level_order([]), 0) == 0
    assert count_paths_with_sum(from_level_order([0, 0, None, 0]), 0) == 6
    assert count_paths_with_sum(from_level_order([1000000000, None, 1000000000]), 2000000000) == 1
    assert count_paths_with_sum(from_level_order([1000000000, None, 1000000000]), -294967296) == 0
    print("119 ok")
