package p105

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

func TestAreIdentical(t *testing.T) {
	cases := []struct {
		a    []int
		b    []int
		want bool
	}{
		{[]int{4, 2, 6}, []int{4, 2, 6}, true},
		{[]int{4, 2}, []int{4, null, 2}, false},
		{[]int{}, []int{}, true},
		{[]int{1}, []int{}, false},
		{[]int{1, 2, 3}, []int{1, 2, 4}, false},
		{[]int{5, null, 8, 7}, []int{5, null, 8, 7}, true},
	}
	for _, c := range cases {
		if got := AreIdentical(fromLevelOrder(c.a), fromLevelOrder(c.b)); got != c.want {
			t.Errorf("AreIdentical(%v, %v) = %t, want %t", c.a, c.b, got, c.want)
		}
	}
}
