// 191 · Requests per Endpoint and Status From Access Logs
package p191

import (
	"cmp"
	"fmt"
	"slices"
	"strings"
)

func CountRequests(lines []string) []string {
	counts := map[string]int{} // "METHOD PATH STATUS" -> requests
	for _, line := range lines {
		fields := strings.Split(line, " ")
		if len(fields) != 5 || slices.Contains(fields, "") {
			continue
		}
		counts[fields[1]+" "+fields[2]+" "+fields[3]]++
	}
	keys := make([]string, 0, len(counts))
	for k := range counts {
		keys = append(keys, k)
	}
	slices.SortFunc(keys, func(a, b string) int {
		return cmp.Or(cmp.Compare(counts[b], counts[a]), strings.Compare(a, b)) // more requests first, then by key
	})
	result := make([]string, len(keys))
	for i, k := range keys {
		result[i] = fmt.Sprintf("%s %d", k, counts[k])
	}
	return result
}
