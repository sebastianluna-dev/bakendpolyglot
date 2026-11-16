package p066

import "testing"

func TestIsBalanced(t *testing.T) {
	cases := []struct {
		s    string
		want bool
	}{
		{"lookup(ids[0], {limit: 5})", true},
		{"([)]", false},
		{"", true},
		{"no brackets", true},
		{"(", false},
		{")", false},
		{"}{", false},
		{"((", false},
		{"{[()()]}", true},
	}
	for _, c := range cases {
		if got := IsBalanced(c.s); got != c.want {
			t.Errorf("IsBalanced(%q) = %v, want %v", c.s, got, c.want)
		}
	}
}
