package p037

import "testing"

func TestToRoman(t *testing.T) {
	cases := []struct {
		n    int
		want string
	}{
		{2026, "MMXXVI"},
		{1994, "MCMXCIV"},
		{1, "I"},
		{4, "IV"},
		{49, "XLIX"},
		{58, "LVIII"},
		{444, "CDXLIV"},
		{3999, "MMMCMXCIX"},
	}
	for _, c := range cases {
		if got := ToRoman(c.n); got != c.want {
			t.Errorf("ToRoman(%d) = %q, want %q", c.n, got, c.want)
		}
	}
}
