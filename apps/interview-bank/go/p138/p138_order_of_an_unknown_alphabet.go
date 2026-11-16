// 138 · Order of an Unknown Alphabet
package p138

func AlphabetOrder(words []string) string {
	var present [26]bool
	letters := 0
	for _, w := range words {
		for k := 0; k < len(w); k++ {
			if !present[w[k]-'a'] {
				letters++
			}
			present[w[k]-'a'] = true
		}
	}
	var before [26][26]bool // before[x][y]: x comes before y
	var waiting [26]int     // letters that must come before this one and are not placed yet
	for i := 0; i+1 < len(words); i++ {
		a, b := words[i], words[i+1]
		j := 0
		for j < len(a) && j < len(b) && a[j] == b[j] {
			j++
		}
		if j == len(a) || j == len(b) {
			if len(a) > len(b) {
				return "" // a word sorted before its own prefix
			}
			continue
		}
		x, y := a[j]-'a', b[j]-'a'
		if !before[x][y] {
			before[x][y] = true
			waiting[y]++
		}
	}
	var placed [26]bool
	order := []byte{}
	for {
		x := 0
		for x < 26 && !(present[x] && !placed[x] && waiting[x] == 0) {
			x++
		}
		if x == 26 {
			break
		}
		placed[x] = true
		order = append(order, byte('a'+x))
		for y := 0; y < 26; y++ {
			if before[x][y] {
				waiting[y]--
			}
		}
	}
	if len(order) != letters {
		return "" // a cycle
	}
	return string(order)
}
