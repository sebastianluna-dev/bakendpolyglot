// 040 · Pack and Unpack a List of Strings
package p040

import (
	"strconv"
	"strings"
)

func Pack(words []string) string {
	var result strings.Builder
	for _, w := range words {
		result.WriteString(strconv.Itoa(len(w)))
		result.WriteByte('#')
		result.WriteString(w)
	}
	return result.String()
}

func Unpack(packed string) []string {
	words := []string{}
	i := 0
	for i < len(packed) {
		length := 0
		for packed[i] != '#' {
			length = length*10 + int(packed[i]-'0')
			i++
		}
		i++ // skip the '#'
		words = append(words, packed[i:i+length])
		i += length
	}
	return words
}
