package p121

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

func TestMaxPathSum(t *testing.T) {
	cases := []struct {
		tree []int
		want int
	}{
		{[]int{6, -8, 3, null, 9, -1, 4}, 14},
		{[]int{-3, -1, -2}, -1},
		{[]int{-5}, -5},
		{[]int{-10, 4, -1, 6, 7}, 17},
		{[]int{2, -1, null, 5}, 6},
		{[]int{1, -2, 3, 4, 5, -6, 2}, 9},
	}
	for _, c := range cases {
		if got := MaxPathSum(fromLevelOrder(c.tree)); got != c.want {
			t.Errorf("MaxPathSum(%v) = %d, want %d", c.tree, got, c.want)
		}
	}
}
