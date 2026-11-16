package p046

import "testing"

func TestIsSubsequence(t *testing.T) {
	cases := []struct {
		plan, scans string
		want        bool
	}{
		{"ACE", "ABCDE", true},
		{"AEC", "ABCDE", false},
		{"", "ABC", true},
		{"A", "", false},
		{"AA", "ABA", true},
		{"AAB", "ABAC", false},
	}
	for _, c := range cases {
		if got := IsSubsequence(c.plan, c.scans); got != c.want {
			t.Errorf("IsSubsequence(%q, %q) = %v, want %v", c.plan, c.scans, got, c.want)
		}
	}
}
