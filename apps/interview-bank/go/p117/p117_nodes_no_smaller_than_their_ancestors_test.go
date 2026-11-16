package p117

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

func TestCountGoodNodes(t *testing.T) {
	cases := []struct {
		tree []int
		want int
	}{
		{[]int{5, 3, 8, 6, 2, 5, 9}, 4},
		{[]int{2, 2, 2}, 3},
		{[]int{}, 0},
		{[]int{7}, 1},
		{[]int{9, 8, null, 7}, 1},
		{[]int{-5, -10, -1}, 2},
	}
	for _, c := range cases {
		if got := CountGoodNodes(fromLevelOrder(c.tree)); got != c.want {
			t.Errorf("CountGoodNodes(%v) = %d, want %d", c.tree, got, c.want)
		}
	}
}
