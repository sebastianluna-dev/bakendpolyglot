// 057 · Contains a Scrambled Copy
package p057

func ContainsScrambled(pattern, text string) bool {
	m := len(pattern)
	if m > len(text) {
		return false
	}
	var need, have [26]int // arrays, not slices, so == compares them
	for i := 0; i < m; i++ {
		need[pattern[i]-'a']++
		have[text[i]-'a']++
	}
	if need == have {
		return true
	}
	for i := m; i < len(text); i++ {
		have[text[i]-'a']++   // letter entering the window
		have[text[i-m]-'a']-- // letter leaving it
		if need == have {
			return true
		}
	}
	return false
}
