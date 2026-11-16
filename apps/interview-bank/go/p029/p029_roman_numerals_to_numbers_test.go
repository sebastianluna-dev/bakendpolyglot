package p029

import "testing"

func TestRomanToNumber(t *testing.T) {
	cases := []struct {
		roman string
		want  int
	}{
		{"MMXXVI", 2026},
		{"CDXLIV", 444},
		{"I", 1},
		{"IV", 4},
		{"IX", 9},
		{"XIV", 14},
		{"CM", 900},
		{"MMMCMXCIX", 3999},
	}
	for _, c := range cases {
		if got := RomanToNumber(c.roman); got != c.want {
			t.Errorf("RomanToNumber(%q) = %d, want %d", c.roman, got, c.want)
		}
	}
}
