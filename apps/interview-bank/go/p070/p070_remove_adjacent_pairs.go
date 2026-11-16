// 070 · Remove Adjacent Pairs
package p070

func RemovePairs(s string) string {
	kept := []byte{} // used as a stack: the result so far
	for i := 0; i < len(s); i++ {
		if n := len(kept); n > 0 && kept[n-1] == s[i] {
			kept = kept[:n-1]
		} else {
			kept = append(kept, s[i])
		}
	}
	return string(kept)
}
