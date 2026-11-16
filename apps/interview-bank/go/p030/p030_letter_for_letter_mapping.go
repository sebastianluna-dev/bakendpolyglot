// 030 · Letter-for-Letter Mapping
package p030

func CanMapLetters(source, target string) bool {
	if len(source) != len(target) {
		return false
	}
	forward := map[byte]byte{}
	backward := map[byte]byte{} // stops two letters mapping to the same one
	for i := 0; i < len(source); i++ {
		a, b := source[i], target[i]
		if mapped, ok := forward[a]; ok && mapped != b {
			return false
		}
		if origin, ok := backward[b]; ok && origin != a {
			return false
		}
		forward[a] = b
		backward[b] = a
	}
	return true
}
