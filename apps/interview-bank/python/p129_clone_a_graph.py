# 129 · Clone a Graph
from collections import deque


class GraphNode:
    def __init__(self, val: int) -> None:
        self.val = val
        self.neighbors: list[GraphNode] = []


def clone_graph(node: GraphNode | None) -> GraphNode | None:
    if node is None:
        return None
    copy_of = {node: GraphNode(node.val)}  # original node -> its copy
    queue = deque([node])
    while queue:
        current = queue.popleft()
        for nb in current.neighbors:
            if nb not in copy_of:
                copy_of[nb] = GraphNode(nb.val)
                queue.append(nb)
            copy_of[current].neighbors.append(copy_of[nb])
    return copy_of[node]


def build(adjacency: list[list[int]]) -> GraphNode | None:
    """Node i has value i and the neighbours adjacency[i]; returns node 0."""
    nodes = [GraphNode(i) for i in range(len(adjacency))]
    for i, neighbors in enumerate(adjacency):
        nodes[i].neighbors = [nodes[j] for j in neighbors]
    return nodes[0] if nodes else None


def all_nodes(node: GraphNode | None) -> list[GraphNode]:
    if node is None:
        return []
    found = {node}
    queue = deque([node])
    while queue:
        for nb in queue.popleft().neighbors:
            if nb not in found:
                found.add(nb)
                queue.append(nb)
    return list(found)


def to_adjacency(node: GraphNode | None) -> list[list[int]]:
    nodes = all_nodes(node)
    adjacency: list[list[int]] = [[] for _ in nodes]
    for n in nodes:
        adjacency[n.val] = [nb.val for nb in n.neighbors]
    return adjacency


def check_clone(adjacency: list[list[int]]) -> None:
    original = build(adjacency)
    copy = clone_graph(original)
    assert to_adjacency(copy) == adjacency
    assert not set(all_nodes(original)) & set(all_nodes(copy))


if __name__ == "__main__":
    check_clone([[1, 3], [0, 2], [1, 3], [0, 2]])
    check_clone([[]])
    check_clone([])
    check_clone([[1], [0]])
    check_clone([[1, 2], [0, 2], [0, 1, 3], [2]])
    print("129 ok")
