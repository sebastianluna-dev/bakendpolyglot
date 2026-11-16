package p033

import "testing"

func TestIsRotation(t *testing.T) {
	cases := []struct {
		a, b string
		want bool
	}{
		{"parcel", "celpar", true},
		{"parcel", "parcle", false},
		{"abc", "abc", true},
		{"", "", true},
		{"aa", "a", false},
		{"abab", "baba", true},
	}
	for _, c := range cases {
		if got := IsRotation(c.a, c.b); got != c.want {
			t.Errorf("IsRotation(%q, %q) = %v, want %v", c.a, c.b, got, c.want)
		}
	}
}
