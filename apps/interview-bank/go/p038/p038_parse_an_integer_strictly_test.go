package p038

import "testing"

func TestParseInteger(t *testing.T) {
	cases := []struct {
		s    string
		want int
		ok   bool
	}{
		{"42", 42, true},
		{"-17", -17, true},
		{"+8", 8, true},
		{"0042", 42, true},
		{"-0", 0, true},
		{"2147483647", 2147483647, true},
		{"-2147483648", -2147483648, true},
		{"2147483648", 0, false},
		{"-2147483649", 0, false},
		{"99999999999999999999", 0, false},
		{"", 0, false},
		{"+", 0, false},
		{" 42", 0, false},
		{"42 ", 0, false},
		{"12a", 0, false},
		{"+-3", 0, false},
		{"3.0", 0, false},
	}
	for _, c := range cases {
		if got, ok := ParseInteger(c.s); got != c.want || ok != c.ok {
			t.Errorf("ParseInteger(%q) = %d, %v, want %d, %v", c.s, got, ok, c.want, c.ok)
		}
	}
}
