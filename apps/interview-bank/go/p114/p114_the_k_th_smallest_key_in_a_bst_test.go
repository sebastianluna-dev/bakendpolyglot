package p114

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

func TestKthSmallest(t *testing.T) {
	cases := []struct {
		tree []int
		k    int
		want int
	}{
		{[]int{20, 10, 30, 5, 15, 25, 40}, 3, 15},
		{[]int{6, 3, 8, 1, 4, null, 9, null, 2}, 2, 2},
		{[]int{6, 3, 8, 1, 4, null, 9, null, 2}, 7, 9},
		{[]int{42}, 1, 42},
		{[]int{1, null, 2, null, 3}, 3, 3},
		{[]int{3, 2, null, 1}, 1, 1},
	}
	for _, c := range cases {
		if got := KthSmallest(fromLevelOrder(c.tree), c.k); got != c.want {
			t.Errorf("KthSmallest(%v, %d) = %d, want %d", c.tree, c.k, got, c.want)
		}
	}
}
