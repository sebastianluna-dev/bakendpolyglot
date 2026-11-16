package p003

import "testing"

func TestSameLetters(t *testing.T) {
	cases := []struct {
		a, b string
		want bool
	}{
		{"dusty", "study", true},
		{"aab", "abb", false},
		{"loan", "loans", false},
		{"abc", "abc", true},
		{"", "", true},
	}
	for _, c := range cases {
		if got := SameLetters(c.a, c.b); got != c.want {
			t.Errorf("SameLetters(%q, %q) = %v, want %v", c.a, c.b, got, c.want)
		}
	}
}
