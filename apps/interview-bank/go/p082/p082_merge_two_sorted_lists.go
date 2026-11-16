// 082 · Merge Two Sorted Lists
package p082

type ListNode struct {
	Val  int
	Next *ListNode
}

func MergeSorted(a, b *ListNode) *ListNode {
	dummy := &ListNode{}
	tail := dummy
	for a != nil && b != nil {
		if a.Val <= b.Val {
			tail.Next = a
			tail = a
			a = a.Next
		} else {
			tail.Next = b
			tail = b
			b = b.Next
		}
	}
	if a != nil { // whatever is left is already sorted
		tail.Next = a
	} else {
		tail.Next = b
	}
	return dummy.Next
}
