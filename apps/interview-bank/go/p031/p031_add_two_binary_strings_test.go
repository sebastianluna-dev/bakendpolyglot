package p031

import (
	"strings"
	"testing"
)

func TestAddBinary(t *testing.T) {
	cases := []struct {
		a, b string
		want string
	}{
		{"1011", "111", "10010"},
		{"0", "0", "0"},
		{"1", "1", "10"},
		{"1111", "1", "10000"},
		{"100", "110010", "110110"},
		{strings.Repeat("1", 70), "1", "1" + strings.Repeat("0", 70)},
	}
	for _, c := range cases {
		if got := AddBinary(c.a, c.b); got != c.want {
			t.Errorf("AddBinary(%q, %q) = %q, want %q", c.a, c.b, got, c.want)
		}
	}
}
