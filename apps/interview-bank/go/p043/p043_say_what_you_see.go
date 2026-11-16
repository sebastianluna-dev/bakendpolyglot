// 043 · Say What You See
package p043

import (
	"strconv"
	"strings"
)

func SayWhatYouSee(start string, rounds int) string {
	s := start
	for range rounds {
		var next strings.Builder
		i := 0
		for i < len(s) {
			j := i
			for j < len(s) && s[j] == s[i] {
				j++
			}
			next.WriteString(strconv.Itoa(j - i))
			next.WriteByte(s[i])
			i = j
		}
		s = next.String()
	}
	return s
}
