// 066 · Balanced Brackets
package p066

func IsBalanced(s string) bool {
	closer := map[byte]byte{'(': ')', '[': ']', '{': '}'}
	expected := []byte{} // the closer each open bracket is waiting for
	for i := 0; i < len(s); i++ {
		ch := s[i]
		if c, ok := closer[ch]; ok {
			expected = append(expected, c)
		} else if ch == ')' || ch == ']' || ch == '}' {
			if len(expected) == 0 || expected[len(expected)-1] != ch {
				return false
			}
			expected = expected[:len(expected)-1]
		}
	}
	return len(expected) == 0
}
