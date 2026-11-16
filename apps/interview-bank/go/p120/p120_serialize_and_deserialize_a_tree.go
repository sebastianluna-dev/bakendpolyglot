// 120 · Serialize and Deserialize a Tree
package p120

import (
	"strconv"
	"strings"
)

type TreeNode struct {
	Val   int
	Left  *TreeNode
	Right *TreeNode
}

func Serialize(root *TreeNode) string {
	var tokens []string
	var write func(node *TreeNode)
	write = func(node *TreeNode) {
		if node == nil {
			tokens = append(tokens, "#")
			return
		}
		tokens = append(tokens, strconv.Itoa(node.Val))
		write(node.Left)
		write(node.Right)
	}
	write(root)
	return strings.Join(tokens, ",")
}

func Deserialize(data string) *TreeNode {
	tokens := strings.Split(data, ",")
	next := 0 // index of the next token to read
	var read func() *TreeNode
	read = func() *TreeNode {
		token := tokens[next]
		next++
		if token == "#" {
			return nil
		}
		val, _ := strconv.Atoi(token) // the format guarantees a valid number here
		node := &TreeNode{Val: val}
		node.Left = read()
		node.Right = read()
		return node
	}
	return read()
}
