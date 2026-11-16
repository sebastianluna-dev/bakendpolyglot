// 089 · Deep-Copy a List With Random Links
package p089

type ListNode struct {
	Val    int
	Next   *ListNode
	Random *ListNode
}

func DeepCopy(head *ListNode) *ListNode {
	copyOf := map[*ListNode]*ListNode{} // original node -> its copy
	for node := head; node != nil; node = node.Next {
		copyOf[node] = &ListNode{Val: node.Val}
	}
	for node := head; node != nil; node = node.Next {
		copyOf[node].Next = copyOf[node.Next] // a missing key, nil included, gives nil
		copyOf[node].Random = copyOf[node.Random]
	}
	return copyOf[head]
}
