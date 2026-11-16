// 146 · Rearrange So No Neighbours Match
package p146

import (
	"container/heap"
	"strings"
)

type letter struct {
	left int // copies not yet placed
	ch   byte
}

type letterHeap []letter

func (h letterHeap) Len() int { return len(h) }
func (h letterHeap) Less(i, j int) bool { // most copies first, then the earlier letter
	if h[i].left != h[j].left {
		return h[i].left > h[j].left
	}
	return h[i].ch < h[j].ch
}
func (h letterHeap) Swap(i, j int) { h[i], h[j] = h[j], h[i] }
func (h *letterHeap) Push(x any)   { *h = append(*h, x.(letter)) }
func (h *letterHeap) Pop() any {
	old := *h
	x := old[len(old)-1]
	*h = old[:len(old)-1]
	return x
}

func Rearrange(s string) string {
	var counts [26]int
	for i := 0; i < len(s); i++ {
		counts[s[i]-'a']++
	}
	h := &letterHeap{}
	for i, c := range counts {
		if c > 0 {
			heap.Push(h, letter{c, byte('a' + i)})
		}
	}
	var sb strings.Builder
	held := letter{} // the letter just placed, kept out of the heap for one step
	for h.Len() > 0 {
		top := heap.Pop(h).(letter)
		sb.WriteByte(top.ch)
		if held.left > 0 {
			heap.Push(h, held)
		}
		held = letter{top.left - 1, top.ch}
	}
	if sb.Len() != len(s) {
		return ""
	}
	return sb.String()
}
