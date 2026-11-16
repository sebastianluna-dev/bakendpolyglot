package p177

import "testing"

func TestEditDistance(t *testing.T) {
	cases := []struct {
		source, target string
		want           int
	}{
		{"shelf", "shelves", 3},
		{"loan", "lone", 2},
		{"", "abc", 3},
		{"abc", "", 3},
		{"seat", "seat", 0},
		{"parcel", "pencil", 3},
		{"trucks", "truck", 1},
		{"abc", "cab", 2},
		{"booking", "looking", 1},
	}
	for _, c := range cases {
		if got := EditDistance(c.source, c.target); got != c.want {
			t.Errorf("EditDistance(%q, %q) = %d, want %d", c.source, c.target, got, c.want)
		}
	}
}
