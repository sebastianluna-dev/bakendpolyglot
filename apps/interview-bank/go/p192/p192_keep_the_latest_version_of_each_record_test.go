package p192

import (
	"slices"
	"testing"
)

func TestLatestVersions(t *testing.T) {
	type R = Revision
	cases := []struct {
		revisions []Revision
		want      []Revision
	}{
		{[]R{{7, 1, "created"}, {3, 2, "in transit"}, {7, 3, "delivered"}, {3, 1, "created"}, {7, 2, "in transit"}},
			[]R{{3, 2, "in transit"}, {7, 3, "delivered"}}},
		{[]R{{5, 4, "first"}, {5, 4, "replayed"}, {1, 1, "only"}}, []R{{1, 1, "only"}, {5, 4, "first"}}},
		{[]R{}, []R{}},
		{[]R{{42, 9, "x"}}, []R{{42, 9, "x"}}},
		{[]R{{10, 1, "a"}, {2, 1, "b"}, {10, 2, "c"}, {-4, 3, "d"}, {2, 5, "e"}},
			[]R{{-4, 3, "d"}, {2, 5, "e"}, {10, 2, "c"}}},
	}
	for _, c := range cases {
		if got := LatestVersions(c.revisions); !slices.Equal(got, c.want) {
			t.Errorf("LatestVersions(%v) = %v, want %v", c.revisions, got, c.want)
		}
	}
}
