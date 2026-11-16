package p039

import "testing"

func TestLongestPalindrome(t *testing.T) {
	cases := []struct {
		s    string
		want string
	}{
		{"xabbaracecarz", "racecar"},
		{"abcde", "a"},
		{"xyzzyabba", "yzzy"},
		{"banana", "anana"},
		{"abacab", "bacab"},
		{"aaaa", "aaaa"},
		{"a", "a"},
		{"", ""},
	}
	for _, c := range cases {
		if got := LongestPalindrome(c.s); got != c.want {
			t.Errorf("LongestPalindrome(%q) = %q, want %q", c.s, got, c.want)
		}
	}
}
