package p057

import "testing"

func TestContainsScrambled(t *testing.T) {
	cases := []struct {
		pattern, text string
		want          bool
	}{
		{"loan", "renewalnoted", true},
		{"seat", "stage", false},
		{"abc", "ab", false},
		{"ab", "ab", true},
		{"aab", "baxaba", true},
		{"aab", "abbb", false},
		{"z", "abcz", true},
	}
	for _, c := range cases {
		if got := ContainsScrambled(c.pattern, c.text); got != c.want {
			t.Errorf("ContainsScrambled(%q, %q) = %v, want %v", c.pattern, c.text, got, c.want)
		}
	}
}
