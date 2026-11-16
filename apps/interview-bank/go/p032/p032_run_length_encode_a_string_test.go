package p032

import (
	"strings"
	"testing"
)

func TestRunLengthEncode(t *testing.T) {
	cases := []struct {
		s    string
		want string
	}{
		{"aaabccdddd", "a3b1c2d4"},
		{"abc", "a1b1c1"},
		{"", ""},
		{strings.Repeat("z", 12), "z12"},
		{"aAaa", "a1A1a2"},
		{"abb", "a1b2"},
	}
	for _, c := range cases {
		if got := RunLengthEncode(c.s); got != c.want {
			t.Errorf("RunLengthEncode(%q) = %q, want %q", c.s, got, c.want)
		}
	}
}
