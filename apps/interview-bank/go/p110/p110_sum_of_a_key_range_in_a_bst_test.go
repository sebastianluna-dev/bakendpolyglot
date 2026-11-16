package p110

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

func TestRangeSum(t *testing.T) {
	cases := []struct {
		tree []int
		lo   int
		hi   int
		want int
	}{
		{[]int{20, 10, 30, 5, 15, 25, 40}, 12, 27, 60},
		{[]int{8, 3, 12, null, 6}, 9, 11, 0},
		{[]int{}, 1, 100, 0},
		{[]int{7}, 7, 7, 7},
		{[]int{20, 10, 30, 5, 15, 25, 40}, 1, 100, 145},
		{[]int{20, 10, 30, 5, 15, 25, 40}, 10, 20, 45},
	}
	for _, c := range cases {
		if got := RangeSum(fromLevelOrder(c.tree), c.lo, c.hi); got != c.want {
			t.Errorf("RangeSum(%v, %d, %d) = %d, want %d", c.tree, c.lo, c.hi, got, c.want)
		}
	}
}
