// 005 · The Majority Genre
package p005

func MajorityGenre(loans []string) string {
	counts := map[string]int{}
	for _, genre := range loans {
		counts[genre]++ // a missing key reads as 0
		if counts[genre]*2 > len(loans) {
			return genre
		}
	}
	return ""
}
