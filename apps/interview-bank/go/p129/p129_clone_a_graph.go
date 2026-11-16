// 129 · Clone a Graph
package p129

type GraphNode struct {
	Val       int
	Neighbors []*GraphNode
}

func CloneGraph(node *GraphNode) *GraphNode {
	if node == nil {
		return nil
	}
	copyOf := map[*GraphNode]*GraphNode{node: {Val: node.Val}} // original -> copy
	queue := []*GraphNode{node}
	for len(queue) > 0 {
		current := queue[0]
		queue = queue[1:]
		for _, nb := range current.Neighbors {
			if _, ok := copyOf[nb]; !ok {
				copyOf[nb] = &GraphNode{Val: nb.Val}
				queue = append(queue, nb)
			}
			copyOf[current].Neighbors = append(copyOf[current].Neighbors, copyOf[nb])
		}
	}
	return copyOf[node]
}
