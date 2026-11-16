package p119

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

func TestCountPathsWithSum(t *testing.T) {
	cases := []struct {
		tree   []int
		target int
		want   int
	}{
		{[]int{4, 2, -1, 1, 3, null, 5}, 5, 2},
		{[]int{1, -1, 1, 1}, 1, 4},
		{[]int{}, 0, 0},
		{[]int{0, 0, null, 0}, 0, 6},
		{[]int{1000000000, null, 1000000000}, 2000000000, 1},
		{[]int{1000000000, null, 1000000000}, -294967296, 0},
	}
	for _, c := range cases {
		if got := CountPathsWithSum(fromLevelOrder(c.tree), c.target); got != c.want {
			t.Errorf("CountPathsWithSum(%v, %d) = %d, want %d", c.tree, c.target, got, c.want)
		}
	}
}
