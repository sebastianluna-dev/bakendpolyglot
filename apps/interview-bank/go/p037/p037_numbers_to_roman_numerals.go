// 037 · Numbers to Roman Numerals
package p037

import "strings"

var (
	values  = []int{1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1}
	symbols = []string{"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"}
)

func ToRoman(n int) string {
	var result strings.Builder
	for i, v := range values {
		for n >= v {
			result.WriteString(symbols[i])
			n -= v
		}
	}
	return result.String()
}
