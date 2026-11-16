// 088 · Add Two Numbers Stored as Lists
package p088

type ListNode struct {
	Val  int
	Next *ListNode
}

func AddLists(a, b *ListNode) *ListNode {
	dummy := &ListNode{}
	tail := dummy
	carry := 0
	for a != nil || b != nil || carry > 0 {
		sum := carry
		if a != nil {
			sum += a.Val
			a = a.Next
		}
		if b != nil {
			sum += b.Val
			b = b.Next
		}
		carry = sum / 10
		tail.Next = &ListNode{Val: sum % 10}
		tail = tail.Next
	}
	return dummy.Next
}
