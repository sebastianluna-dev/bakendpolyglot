// 015 · The k Most Borrowed Books
package p015

import (
	"cmp"
	"slices"
)

func MostBorrowed(loans []string, k int) []string {
	counts := map[string]int{}
	for _, title := range loans {
		counts[title]++
	}
	titles := make([]string, 0, len(counts))
	for title := range counts {
		titles = append(titles, title)
	}
	slices.SortFunc(titles, func(a, b string) int {
		if counts[a] != counts[b] {
			return cmp.Compare(counts[b], counts[a])
		}
		return cmp.Compare(a, b)
	})
	return titles[:min(k, len(titles))]
}
