// 116 · Rebuild a Tree From Two Traversals
package p116

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func BuildTree(preorder, inorder []int) *TreeNode {
	position := make(map[int]int, len(inorder))
	for i, value := range inorder {
		position[value] = i
	}

	// builds the subtree whose preorder starts at pre and whose inorder is inorder[lo..hi]
	var build func(pre, lo, hi int) *TreeNode
	build = func(pre, lo, hi int) *TreeNode {
		if lo > hi {
			return nil
		}
		root := &TreeNode{Val: preorder[pre]}
		mid := position[root.Val]
		root.Left = build(pre+1, lo, mid-1)
		root.Right = build(pre+1+(mid-lo), mid+1, hi)
		return root
	}
	return build(0, 0, len(inorder)-1)
}
