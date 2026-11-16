package p138

import "testing"

func TestAlphabetOrder(t *testing.T) {
	cases := []struct {
		words []string
		want  string
	}{
		{[]string{"cab", "cad", "ab", "ba", "bd"}, "cabd"},
		{[]string{"ba", "bc", "ad"}, "bacd"},
		{[]string{"abc", "ab"}, ""},
		{[]string{"a", "b", "a"}, ""},
		{[]string{"a", "b", "c", "a"}, ""},
		{[]string{"zyx"}, "xyz"},
		{[]string{"ab", "ab"}, "ab"},
		{[]string{"ab", "abc"}, "abc"},
		{[]string{"x", "z", "y"}, "xzy"},
	}
	for _, c := range cases {
		if got := AlphabetOrder(c.words); got != c.want {
			t.Errorf("AlphabetOrder(%q) = %q, want %q", c.words, got, c.want)
		}
	}
}
