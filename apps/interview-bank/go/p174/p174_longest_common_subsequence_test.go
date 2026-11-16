package p174

import "testing"

func TestLongestCommonSubsequence(t *testing.T) {
	cases := []struct {
		a, b string
		want int
	}{
		{"parcel", "place", 4},
		{"seat", "lock", 0},
		{"", "abc", 0},
		{"abc", "", 0},
		{"member", "member", 6},
		{"aaaa", "aa", 2},
		{"delivery", "relived", 5},
	}
	for _, c := range cases {
		if got := LongestCommonSubsequence(c.a, c.b); got != c.want {
			t.Errorf("LongestCommonSubsequence(%q, %q) = %d, want %d", c.a, c.b, got, c.want)
		}
	}
}
