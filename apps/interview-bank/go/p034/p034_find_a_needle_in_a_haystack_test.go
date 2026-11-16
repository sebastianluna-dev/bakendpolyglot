package p034

import "testing"

func TestFindNeedle(t *testing.T) {
	cases := []struct {
		haystack, needle string
		want             int
	}{
		{"hold-14-seat-14", "14", 5},
		{"parcel", "pulse", -1},
		{"abc", "c", 2},
		{"aaab", "ab", 2},
		{"abc", "", 0},
		{"", "", 0},
		{"", "a", -1},
		{"ab", "abc", -1},
	}
	for _, c := range cases {
		if got := FindNeedle(c.haystack, c.needle); got != c.want {
			t.Errorf("FindNeedle(%q, %q) = %d, want %d", c.haystack, c.needle, got, c.want)
		}
	}
}
