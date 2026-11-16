// 090 · Reverse in Groups of k
package p090

type ListNode struct {
	Val  int
	Next *ListNode
}

func ReverseInGroups(head *ListNode, k int) *ListNode {
	dummy := &ListNode{Next: head}
	before := dummy // the node just before the next group
	for {
		last := before
		for i := 0; i < k && last != nil; i++ {
			last = last.Next
		}
		if last == nil {
			return dummy.Next // fewer than k nodes left: keep them as they are
		}
		after := last.Next
		first := before.Next
		prev, node := after, first
		for node != after {
			next := node.Next
			node.Next = prev
			prev = node
			node = next
		}
		before.Next = last
		before = first
	}
}
