package p074

import "testing"

func TestExpandPatterns(t *testing.T) {
	cases := []struct {
		s    string
		want string
	}{
		{"3[ab]2[c]", "abababcc"},
		{"x2[y3[z]]", "xyzzzyzzz"},
		{"", ""},
		{"plain", "plain"},
		{"12[q]", "qqqqqqqqqqqq"},
		{"1[a1[b1[c]]]d", "abcd"},
		{"2[a2[b]c]", "abbcabbc"},
	}
	for _, c := range cases {
		if got := ExpandPatterns(c.s); got != c.want {
			t.Errorf("ExpandPatterns(%q) = %q, want %q", c.s, got, c.want)
		}
	}
}
