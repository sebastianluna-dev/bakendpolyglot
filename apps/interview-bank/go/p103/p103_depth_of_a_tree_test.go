package p103

import (
	"math"
	"testing"
)

const null = math.MinInt // marks a missing child in a level-order slice

func fromLevelOrder(values []int) *TreeNode {
	if len(values) == 0 || values[0] == null {
		return nil
	}
	root := &TreeNode{Val: values[0]}
	queue := []*TreeNode{root}
	for i := 1; i < len(values); i += 2 {
		node := queue[0]
		queue = queue[1:]
		if values[i] != null {
			node.Left = &TreeNode{Val: values[i]}
			queue = append(queue, node.Left)
		}
		if i+1 < len(values) && values[i+1] != null {
			node.Right = &TreeNode{Val: values[i+1]}
			queue = append(queue, node.Right)
		}
	}
	return root
}

func TestMaxDepth(t *testing.T) {
	cases := []struct {
		tree []int
		want int
	}{
		{[]int{5, 2, 9, null, 4, 7, null, null, null, 6}, 4},
		{[]int{1, null, 2, null, 3}, 3},
		{[]int{}, 0},
		{[]int{42}, 1},
		{[]int{1, 2, 3}, 2},
	}
	for _, c := range cases {
		if got := MaxDepth(fromLevelOrder(c.tree)); got != c.want {
			t.Errorf("MaxDepth(%v) = %d, want %d", c.tree, got, c.want)
		}
	}
}
