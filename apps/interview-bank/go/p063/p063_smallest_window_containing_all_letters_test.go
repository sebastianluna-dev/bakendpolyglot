package p063

import "testing"

func TestSmallestWindow(t *testing.T) {
	cases := []struct {
		text, letters, want string
	}{
		{"deliverytruck", "tire", "iveryt"},
		{"banana", "nn", "nan"},
		{"abba", "ab", "ab"},
		{"abc", "abd", ""},
		{"a", "aa", ""},
		{"Aa", "a", "a"},
		{"", "a", ""},
		{"parcel", "parcel", "parcel"},
	}
	for _, c := range cases {
		if got := SmallestWindow(c.text, c.letters); got != c.want {
			t.Errorf("SmallestWindow(%q, %q) = %q, want %q", c.text, c.letters, got, c.want)
		}
	}
}
