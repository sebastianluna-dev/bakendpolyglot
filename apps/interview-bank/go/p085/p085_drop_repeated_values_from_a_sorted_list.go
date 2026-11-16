// 085 · Drop Repeated Values From a Sorted List
package p085

type ListNode struct {
	Val  int
	Next *ListNode
}

func DropRepeats(head *ListNode) *ListNode {
	node := head
	for node != nil && node.Next != nil {
		if node.Next.Val == node.Val {
			node.Next = node.Next.Next // stay: the new next may repeat too
		} else {
			node = node.Next
		}
	}
	return head
}
