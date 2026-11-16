package p152

import (
	"slices"
	"testing"
)

func TestAllSpellings(t *testing.T) {
	cases := []struct {
		code string
		want []string
	}{
		{"b7x", []string{"B7X", "B7x", "b7X", "b7x"}},
		{"42", []string{"42"}},
		{"Q", []string{"Q", "q"}},
		{"aB1", []string{"AB1", "Ab1", "aB1", "ab1"}},
		{"0z0", []string{"0Z0", "0z0"}},
	}
	for _, c := range cases {
		if got := AllSpellings(c.code); !slices.Equal(got, c.want) {
			t.Errorf("AllSpellings(%q) = %v, want %v", c.code, got, c.want)
		}
	}
	if got := len(AllSpellings("abcdefghij")); got != 1024 {
		t.Errorf("len(AllSpellings(\"abcdefghij\")) = %d, want 1024", got)
	}
}
