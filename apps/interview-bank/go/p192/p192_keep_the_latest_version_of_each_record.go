// 192 · Keep the Latest Version of Each Record
package p192

import (
	"cmp"
	"slices"
)

type Revision struct {
	ID      int
	Version int
	Payload string
}

func LatestVersions(revisions []Revision) []Revision {
	latest := map[int]Revision{}
	for _, r := range revisions {
		if kept, ok := latest[r.ID]; !ok || r.Version > kept.Version { // strictly greater: the first of equal versions stays
			latest[r.ID] = r
		}
	}
	result := make([]Revision, 0, len(latest))
	for _, r := range latest {
		result = append(result, r)
	}
	slices.SortFunc(result, func(a, b Revision) int { return cmp.Compare(a.ID, b.ID) })
	return result
}
