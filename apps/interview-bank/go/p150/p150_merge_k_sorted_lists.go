// 150 · Merge k Sorted Lists
package p150

import "container/heap"

type ListNode struct {
	Val  int
	Next *ListNode
}

type nodeHeap []*ListNode // the front node of each list

func (h nodeHeap) Len() int           { return len(h) }
func (h nodeHeap) Less(i, j int) bool { return h[i].Val < h[j].Val }
func (h nodeHeap) Swap(i, j int)      { h[i], h[j] = h[j], h[i] }
func (h *nodeHeap) Push(x any)        { *h = append(*h, x.(*ListNode)) }
func (h *nodeHeap) Pop() any {
	old := *h
	x := old[len(old)-1]
	*h = old[:len(old)-1]
	return x
}

func MergeLists(lists []*ListNode) *ListNode {
	h := &nodeHeap{}
	for _, head := range lists {
		if head != nil {
			heap.Push(h, head)
		}
	}
	dummy := &ListNode{}
	tail := dummy
	for h.Len() > 0 {
		node := heap.Pop(h).(*ListNode)
		tail.Next = node
		tail = node
		if node.Next != nil {
			heap.Push(h, node.Next)
		}
	}
	return dummy.Next
}
