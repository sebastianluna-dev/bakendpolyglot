// 003 · Same Letters, Different Order
package p003

func SameLetters(a, b string) bool {
	if len(a) != len(b) {
		return false
	}
	var counts [26]int
	for i := 0; i < len(a); i++ { // indexing a string gives bytes, fine for a-z
		counts[a[i]-'a']++
		counts[b[i]-'a']--
	}
	for _, c := range counts {
		if c != 0 {
			return false
		}
	}
	return true
}
