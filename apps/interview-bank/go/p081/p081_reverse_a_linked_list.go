// 081 · Reverse a Linked List
package p081

type ListNode struct {
	Val  int
	Next *ListNode
}

func ReverseList(head *ListNode) *ListNode {
	var prev *ListNode
	node := head
	for node != nil {
		next := node.Next // save it before the link is overwritten
		node.Next = prev
		prev = node
		node = next
	}
	return prev
}
