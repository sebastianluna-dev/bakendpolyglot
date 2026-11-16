// 044 · Fully Justify Text
package p044

import "strings"

func JustifyText(words []string, width int) []string {
	lines := []string{}
	i := 0
	for i < len(words) {
		j := i + 1
		used := len(words[i]) // the words plus one space between each pair
		for j < len(words) && used+1+len(words[j]) <= width {
			used += 1 + len(words[j])
			j++
		}
		gaps := j - i - 1
		if j == len(words) || gaps == 0 {
			line := strings.Join(words[i:j], " ")
			lines = append(lines, line+strings.Repeat(" ", width-len(line)))
		} else {
			spaces := width - (used - gaps)
			each, extra := spaces/gaps, spaces%gaps
			var line strings.Builder
			for k := i; k < j-1; k++ {
				line.WriteString(words[k])
				n := each
				if k-i < extra {
					n++
				}
				line.WriteString(strings.Repeat(" ", n))
			}
			line.WriteString(words[j-1])
			lines = append(lines, line.String())
		}
		i = j
	}
	return lines
}
