// 122 · A Word Dictionary With Wildcards
package p122

type trieNode struct {
	children [26]*trieNode
	isWord   bool
}

type WordDictionary struct {
	root *trieNode
}

func NewWordDictionary() *WordDictionary {
	return &WordDictionary{root: &trieNode{}}
}

func (d *WordDictionary) AddWord(word string) {
	node := d.root
	for i := 0; i < len(word); i++ {
		c := word[i] - 'a'
		if node.children[c] == nil {
			node.children[c] = &trieNode{}
		}
		node = node.children[c]
	}
	node.isWord = true
}

func (d *WordDictionary) Search(pattern string) bool {
	return match(d.root, pattern, 0)
}

func match(node *trieNode, pattern string, i int) bool {
	if i == len(pattern) {
		return node.isWord
	}
	if pattern[i] == '.' {
		for _, child := range node.children {
			if child != nil && match(child, pattern, i+1) {
				return true
			}
		}
		return false
	}
	next := node.children[pattern[i]-'a']
	return next != nil && match(next, pattern, i+1)
}
