// 074 · Expand Repeated Patterns
package p074

import "strings"

func ExpandPatterns(s string) string {
	counts := []int{}
	outer := []string{} // the text built before each open bracket
	current := ""
	count := 0
	for i := 0; i < len(s); i++ {
		ch := s[i]
		switch {
		case ch >= '0' && ch <= '9':
			count = count*10 + int(ch-'0')
		case ch == '[':
			counts = append(counts, count)
			outer = append(outer, current)
			current = ""
			count = 0
		case ch == ']':
			k := counts[len(counts)-1]
			counts = counts[:len(counts)-1]
			current = outer[len(outer)-1] + strings.Repeat(current, k)
			outer = outer[:len(outer)-1]
		default:
			current += string(ch)
		}
	}
	return current
}
