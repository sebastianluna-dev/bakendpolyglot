// 036 · Reverse the Words of a Sentence
package p036

import (
	"slices"
	"strings"
)

func ReverseWords(sentence string) string {
	words := strings.Fields(sentence) // splits on runs of spaces and drops the empty pieces
	slices.Reverse(words)
	return strings.Join(words, " ")
}
