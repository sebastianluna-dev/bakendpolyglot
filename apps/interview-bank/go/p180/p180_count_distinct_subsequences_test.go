package p180

import (
	"strings"
	"testing"
)

func TestCountSubsequences(t *testing.T) {
	cases := []struct {
		s, t string
		want int
	}{
		{"banana", "ban", 3},
		{"seatseat", "sat", 4},
		{"abc", "abcd", 0},
		{"abc", "", 1},
		{"", "a", 0},
		{"", "", 1},
		{"aaaa", "aa", 6},
		{"a", "aa", 0},
		{strings.Repeat("a", 1000), strings.Repeat("a", 500), 159835829},
	}
	for _, c := range cases {
		if got := CountSubsequences(c.s, c.t); got != c.want {
			t.Errorf("CountSubsequences(%q, %q) = %d, want %d", c.s, c.t, got, c.want)
		}
	}
}
