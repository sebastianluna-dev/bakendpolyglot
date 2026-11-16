package p045

import "testing"

func TestShortestPalindrome(t *testing.T) {
	cases := []struct {
		s    string
		want string
	}{
		{"abacd", "dcabacd"},
		{"stats", "stats"},
		{"", ""},
		{"a", "a"},
		{"ab", "bab"},
		{"aabba", "abbaabba"},
		{"aaa", "aaa"},
	}
	for _, c := range cases {
		if got := ShortestPalindrome(c.s); got != c.want {
			t.Errorf("ShortestPalindrome(%q) = %q, want %q", c.s, got, c.want)
		}
	}
}
