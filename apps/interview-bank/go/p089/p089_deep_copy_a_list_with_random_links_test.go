package p089

import (
	"slices"
	"testing"
)

// fromPairs builds the list from {value, index of the random target} pairs, -1 for no target.
func fromPairs(pairs [][2]int) *ListNode {
	if len(pairs) == 0 {
		return nil
	}
	nodes := make([]*ListNode, len(pairs))
	for i, p := range pairs {
		nodes[i] = &ListNode{Val: p[0]}
	}
	for i, p := range pairs {
		if i+1 < len(nodes) {
			nodes[i].Next = nodes[i+1]
		}
		if p[1] >= 0 {
			nodes[i].Random = nodes[p[1]]
		}
	}
	return nodes[0]
}

func toPairs(head *ListNode) [][2]int {
	index := map[*ListNode]int{}
	i := 0
	for node := head; node != nil; node = node.Next {
		index[node] = i
		i++
	}
	pairs := [][2]int{}
	for node := head; node != nil; node = node.Next {
		target := -1
		if node.Random != nil {
			target = index[node.Random]
		}
		pairs = append(pairs, [2]int{node.Val, target})
	}
	return pairs
}

func sharesNode(a, b *ListNode) bool {
	seen := map[*ListNode]bool{}
	for node := a; node != nil; node = node.Next {
		seen[node] = true
	}
	for node := b; node != nil; node = node.Next {
		if seen[node] || seen[node.Random] {
			return true
		}
	}
	return false
}

func TestDeepCopy(t *testing.T) {
	cases := [][][2]int{
		{{4, 2}, {9, 0}, {6, -1}},
		{{1, 1}, {2, 1}},
		{},
		{{5, 0}},
		{{7, 1}, {7, 0}},
		{{3, -1}, {8, -1}, {2, -1}},
	}
	for _, pairs := range cases {
		original := fromPairs(pairs)
		copied := DeepCopy(original)
		if got := toPairs(copied); !slices.Equal(got, pairs) {
			t.Errorf("DeepCopy(%v) = %v", pairs, got)
		}
		if got := toPairs(original); !slices.Equal(got, pairs) {
			t.Errorf("DeepCopy(%v) changed the original to %v", pairs, got)
		}
		if sharesNode(original, copied) {
			t.Errorf("DeepCopy(%v) shares nodes with the original", pairs)
		}
	}
}
