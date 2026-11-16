// 112 · What You See From the Right
package p112

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func RightSideView(root *TreeNode) []int {
	view := []int{}
	var visit func(node *TreeNode, depth int)
	visit = func(node *TreeNode, depth int) {
		if node == nil {
			return
		}
		if depth == len(view) {
			view = append(view, node.Val) // first node reached at this depth
		}
		visit(node.Right, depth+1)
		visit(node.Left, depth+1)
	}
	visit(root, 0)
	return view
}
