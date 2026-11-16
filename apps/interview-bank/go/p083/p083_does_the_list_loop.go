// 083 · Does the List Loop?
package p083

type ListNode struct {
	Val  int
	Next *ListNode
}

func HasLoop(head *ListNode) bool {
	slow, fast := head, head
	for fast != nil && fast.Next != nil {
		slow = slow.Next
		fast = fast.Next.Next
		if slow == fast {
			return true
		}
	}
	return false
}
