package p178

import "testing"

func TestMatchesPattern(t *testing.T) {
	cases := []struct {
		text, pattern string
		want          bool
	}{
		{"seat", "se.*t", true},
		{"seats", "se.*t", false},
		{"", "a*b*", true},
		{"", "", true},
		{"a", "", false},
		{"", ".", false},
		{"xxy", "z*x*y", true},
		{"ac", "a.c", false},
		{"aaa", "a*a", true},
		{"bbb", "bc*b*d*b", true},
		{"b", "a*b", true},
		{"ab", ".*", true},
		{"abcd", "d*", false},
		{"aaaaaaaaaaaaaaaaaaab", "a*a*a*a*a*a*a*a*c", false},
	}
	for _, c := range cases {
		if got := MatchesPattern(c.text, c.pattern); got != c.want {
			t.Errorf("MatchesPattern(%q, %q) = %t, want %t", c.text, c.pattern, got, c.want)
		}
	}
}
