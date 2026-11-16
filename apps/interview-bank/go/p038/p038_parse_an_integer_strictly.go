// 038 · Parse an Integer Strictly
package p038

func ParseInteger(s string) (int, bool) {
	i := 0
	negative := false
	if len(s) > 0 && (s[0] == '+' || s[0] == '-') {
		negative = s[0] == '-'
		i = 1
	}
	if i == len(s) {
		return 0, false // empty, or a sign with no digits
	}
	limit := 2147483647
	if negative {
		limit = 2147483648
	}
	value := 0
	for ; i < len(s); i++ {
		ch := s[i]
		if ch < '0' || ch > '9' {
			return 0, false
		}
		value = value*10 + int(ch-'0')
		if value > limit {
			return 0, false
		}
	}
	if negative {
		return -value, true
	}
	return value, true
}
