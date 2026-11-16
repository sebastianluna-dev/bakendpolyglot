// 087 · Interleave a List With Its Reverse
package p087

type ListNode struct {
	Val  int
	Next *ListNode
}

func InterleaveWithReverse(head *ListNode) {
	if head == nil {
		return
	}
	slow, fast := head, head
	for fast.Next != nil && fast.Next.Next != nil {
		slow = slow.Next
		fast = fast.Next.Next
	}
	second := reverse(slow.Next)
	slow.Next = nil // cut the first half off
	first := head
	for first != nil && second != nil {
		nextFirst, nextSecond := first.Next, second.Next
		first.Next = second
		second.Next = nextFirst
		first, second = nextFirst, nextSecond
	}
}

func reverse(head *ListNode) *ListNode {
	var prev *ListNode
	node := head
	for node != nil {
		next := node.Next
		node.Next = prev
		prev = node
		node = next
	}
	return prev
}
