// 086 · Remove the k-th Node From the End
package p086

type ListNode struct {
	Val  int
	Next *ListNode
}

func RemoveKthFromEnd(head *ListNode, k int) *ListNode {
	dummy := &ListNode{Next: head} // the node before the head, in case the head goes
	lead := head
	for range k {
		lead = lead.Next
	}
	trail := dummy
	for lead != nil {
		lead = lead.Next
		trail = trail.Next
	}
	trail.Next = trail.Next.Next
	return dummy.Next
}
