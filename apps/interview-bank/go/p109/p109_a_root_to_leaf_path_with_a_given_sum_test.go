package p109

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

func TestHasPathSum(t *testing.T) {
	cases := []struct {
		tree   []int
		target int
		want   bool
	}{
		{[]int{5, 3, 8, 1, null, null, 4}, 9, true},
		{[]int{2, 7}, 2, false},
		{[]int{}, 0, false},
		{[]int{5, 3, 8, 1, null, null, 4}, 17, true},
		{[]int{5, 3, 8, 1, null, null, 4}, 8, false},
		{[]int{-3, 1, -2}, -5, true},
	}
	for _, c := range cases {
		if got := HasPathSum(fromLevelOrder(c.tree), c.target); got != c.want {
			t.Errorf("HasPathSum(%v, %d) = %t, want %t", c.tree, c.target, got, c.want)
		}
	}
}
