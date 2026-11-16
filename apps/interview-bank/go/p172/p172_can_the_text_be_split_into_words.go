// 172 · Can the Text Be Split Into Words?
package p172

func CanSplit(text string, words []string) bool {
	dictionary := map[string]bool{}
	longest := 0
	for _, w := range words {
		dictionary[w] = true
		longest = max(longest, len(w))
	}
	splits := make([]bool, len(text)+1) // splits[i]: the first i letters split into words
	splits[0] = true
	for end := 1; end <= len(text); end++ {
		for l := 1; l <= min(end, longest); l++ {
			if splits[end-l] && dictionary[text[end-l:end]] {
				splits[end] = true
				break
			}
		}
	}
	return splits[len(text)]
}
