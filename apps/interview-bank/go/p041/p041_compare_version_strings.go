// 041 · Compare Version Strings
package p041

func CompareVersions(a, b string) int {
	i, j := 0, 0
	for i < len(a) || j < len(b) {
		x := 0
		for i < len(a) && a[i] != '.' {
			x = x*10 + int(a[i]-'0')
			i++
		}
		y := 0
		for j < len(b) && b[j] != '.' {
			y = y*10 + int(b[j]-'0')
			j++
		}
		if x != y {
			if x < y {
				return -1
			}
			return 1
		}
		i++ // step over the dot; past the end, the next part reads as 0
		j++
	}
	return 0
}
