// 014 · Group Words by Their Letters
package p014

import "slices"

func GroupByLetters(words []string) [][]string {
	groupOf := map[string]int{} // sorted letters -> index in groups
	groups := [][]string{}
	for _, word := range words {
		letters := []byte(word)
		slices.Sort(letters)
		key := string(letters)
		g, ok := groupOf[key]
		if !ok {
			g = len(groups)
			groupOf[key] = g
			groups = append(groups, []string{})
		}
		groups[g] = append(groups[g], word)
	}
	return groups
}
