// 032 · Run-Length Encode a String
package p032

import (
	"strconv"
	"strings"
)

func RunLengthEncode(s string) string {
	var result strings.Builder
	i := 0
	for i < len(s) {
		j := i
		for j < len(s) && s[j] == s[i] {
			j++
		}
		result.WriteByte(s[i])
		result.WriteString(strconv.Itoa(j - i))
		i = j
	}
	return result.String()
}
