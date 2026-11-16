package p129

import (
	"reflect"
	"testing"
)

// build makes node i with value i and the neighbours adjacency[i], and returns node 0.
func build(adjacency [][]int) *GraphNode {
	if len(adjacency) == 0 {
		return nil
	}
	nodes := make([]*GraphNode, len(adjacency))
	for i := range nodes {
		nodes[i] = &GraphNode{Val: i}
	}
	for i, neighbors := range adjacency {
		for _, j := range neighbors {
			nodes[i].Neighbors = append(nodes[i].Neighbors, nodes[j])
		}
	}
	return nodes[0]
}

func allNodes(node *GraphNode) map[*GraphNode]bool {
	found := map[*GraphNode]bool{}
	if node == nil {
		return found
	}
	found[node] = true
	queue := []*GraphNode{node}
	for len(queue) > 0 {
		current := queue[0]
		queue = queue[1:]
		for _, nb := range current.Neighbors {
			if !found[nb] {
				found[nb] = true
				queue = append(queue, nb)
			}
		}
	}
	return found
}

func toAdjacency(node *GraphNode) [][]int {
	nodes := allNodes(node)
	adjacency := make([][]int, len(nodes))
	for n := range nodes {
		adjacency[n.Val] = []int{}
		for _, nb := range n.Neighbors {
			adjacency[n.Val] = append(adjacency[n.Val], nb.Val)
		}
	}
	return adjacency
}

func TestCloneGraph(t *testing.T) {
	cases := [][][]int{
		{{1, 3}, {0, 2}, {1, 3}, {0, 2}},
		{{}},
		{},
		{{1}, {0}},
		{{1, 2}, {0, 2}, {0, 1, 3}, {2}},
	}
	for _, adjacency := range cases {
		original := build(adjacency)
		copied := CloneGraph(original)
		if got := toAdjacency(copied); !reflect.DeepEqual(got, adjacency) {
			t.Errorf("CloneGraph(%v) = %v", adjacency, got)
		}
		originals := allNodes(original)
		for n := range allNodes(copied) {
			if originals[n] {
				t.Errorf("CloneGraph(%v) shares nodes with the original", adjacency)
			}
		}
	}
}
