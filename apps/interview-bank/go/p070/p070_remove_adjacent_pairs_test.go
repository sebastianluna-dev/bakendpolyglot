package p070

import "testing"

func TestRemovePairs(t *testing.T) {
	cases := []struct {
		s, want string
	}{
		{"abccbd", "ad"},
		{"aabb", ""},
		{"", ""},
		{"abc", "abc"},
		{"aaa", "a"},
		{"abba", ""},
		{"mississippi", "m"},
	}
	for _, c := range cases {
		if got := RemovePairs(c.s); got != c.want {
			t.Errorf("RemovePairs(%q) = %q, want %q", c.s, got, c.want)
		}
	}
}
