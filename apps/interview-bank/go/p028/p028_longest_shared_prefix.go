// 028 · Longest Shared Prefix
package p028

func LongestSharedPrefix(words []string) string {
	if len(words) == 0 {
		return ""
	}
	first := words[0]
	for i := 0; i < len(first); i++ {
		for _, word := range words {
			if i == len(word) || word[i] != first[i] {
				return first[:i]
			}
		}
	}
	return first
}
