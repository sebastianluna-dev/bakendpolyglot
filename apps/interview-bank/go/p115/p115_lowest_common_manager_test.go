package p115

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

func TestLowestCommonManager(t *testing.T) {
	chart := []int{1, 2, 3, 4, 5, 6, 7, null, null, 8, 9}
	cases := []struct {
		tree []int
		a, b int
		want int
	}{
		{chart, 8, 4, 2},
		{chart, 9, 7, 1},
		{chart, 5, 9, 5},
		{chart, 6, 6, 6},
		{chart, 8, 9, 5},
		{[]int{1, 2}, 2, 1, 1},
	}
	for _, c := range cases {
		if got := LowestCommonManager(fromLevelOrder(c.tree), c.a, c.b); got != c.want {
			t.Errorf("LowestCommonManager(%v, %d, %d) = %d, want %d", c.tree, c.a, c.b, got, c.want)
		}
	}
}
