package p112

import (
	"math"
	"slices"
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

func TestRightSideView(t *testing.T) {
	cases := []struct {
		tree []int
		want []int
	}{
		{[]int{10, 4, 15, 2, 6, null, 20, null, null, 5}, []int{10, 15, 20, 5}},
		{[]int{1, 2}, []int{1, 2}},
		{[]int{}, []int{}},
		{[]int{4}, []int{4}},
		{[]int{1, 2, null, 3}, []int{1, 2, 3}},
		{[]int{1, 2, 3, 4, 5, 6, 7}, []int{1, 3, 7}},
	}
	for _, c := range cases {
		if got := RightSideView(fromLevelOrder(c.tree)); !slices.Equal(got, c.want) {
			t.Errorf("RightSideView(%v) = %v, want %v", c.tree, got, c.want)
		}
	}
}
