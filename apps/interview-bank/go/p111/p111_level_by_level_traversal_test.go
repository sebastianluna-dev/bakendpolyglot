package p111

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

func TestLevelOrder(t *testing.T) {
	cases := []struct {
		tree []int
		want [][]int
	}{
		{[]int{8, 4, 12, 2, null, 10, 14}, [][]int{{8}, {4, 12}, {2, 10, 14}}},
		{[]int{5, 6, null, 7, 8}, [][]int{{5}, {6}, {7, 8}}},
		{[]int{}, [][]int{}},
		{[]int{9}, [][]int{{9}}},
		{[]int{1, 2, 3, null, 4, 5, null, null, null, 6}, [][]int{{1}, {2, 3}, {4, 5}, {6}}},
	}
	for _, c := range cases {
		if got := LevelOrder(fromLevelOrder(c.tree)); !slices.EqualFunc(got, c.want, slices.Equal[[]int]) {
			t.Errorf("LevelOrder(%v) = %v, want %v", c.tree, got, c.want)
		}
	}
}
