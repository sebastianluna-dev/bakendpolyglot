package p026

import "testing"

func TestIsPalindrome(t *testing.T) {
	cases := []struct {
		text string
		want bool
	}{
		{"Step on no pets!", true},
		{"Pets on no steps", false},
		{"", true},
		{",.! ?", true},
		{"a.", true},
		{"Ab1bA", true},
		{"1a2", false},
		{"0P", false},
	}
	for _, c := range cases {
		if got := IsPalindrome(c.text); got != c.want {
			t.Errorf("IsPalindrome(%q) = %v, want %v", c.text, got, c.want)
		}
	}
}
