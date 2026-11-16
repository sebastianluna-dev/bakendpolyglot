package p055

import "testing"

func TestLongestWithoutRepeat(t *testing.T) {
	cases := []struct {
		s    string
		want int
	}{
		{"shelfwise", 7},
		{"aaaa", 1},
		{"", 0},
		{"abba", 2},
		{"dvdf", 3},
		{"A-1 a-2", 5},
	}
	for _, c := range cases {
		if got := LongestWithoutRepeat(c.s); got != c.want {
			t.Errorf("LongestWithoutRepeat(%q) = %d, want %d", c.s, got, c.want)
		}
	}
}
