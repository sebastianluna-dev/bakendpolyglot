package p030

import "testing"

func TestCanMapLetters(t *testing.T) {
	cases := []struct {
		source, target string
		want           bool
	}{
		{"deed", "noon", true},
		{"bell", "cool", false},
		{"", "", true},
		{"a", "bc", false},
		{"abc", "bca", true},
		{"ab", "aa", false},
		{"aa", "ab", false},
		{"tab", "tat", false},
	}
	for _, c := range cases {
		if got := CanMapLetters(c.source, c.target); got != c.want {
			t.Errorf("CanMapLetters(%q, %q) = %v, want %v", c.source, c.target, got, c.want)
		}
	}
}
