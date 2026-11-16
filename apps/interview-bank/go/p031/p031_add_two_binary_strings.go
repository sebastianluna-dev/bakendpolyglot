// 031 · Add Two Binary Strings
package p031

import "slices"

func AddBinary(a, b string) string {
	digits := []byte{}
	i, j, carry := len(a)-1, len(b)-1, 0
	for i >= 0 || j >= 0 || carry > 0 {
		total := carry
		if i >= 0 {
			total += int(a[i] - '0')
			i--
		}
		if j >= 0 {
			total += int(b[j] - '0')
			j--
		}
		digits = append(digits, byte('0'+total%2))
		carry = total / 2
	}
	slices.Reverse(digits)
	return string(digits)
}
