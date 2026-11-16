package p159

import (
	"slices"
	"testing"
)

func TestKeypadWords(t *testing.T) {
	cases := []struct {
		digits string
		want   []string
	}{
		{"23", []string{"ad", "ae", "af", "bd", "be", "bf", "cd", "ce", "cf"}},
		{"7", []string{"p", "q", "r", "s"}},
		{"", []string{}},
		{"9", []string{"w", "x", "y", "z"}},
	}
	for _, c := range cases {
		if got := KeypadWords(c.digits); !slices.Equal(got, c.want) {
			t.Errorf("KeypadWords(%q) = %v, want %v", c.digits, got, c.want)
		}
	}
	words := KeypadWords("79")
	if len(words) != 16 || words[0] != "pw" || words[len(words)-1] != "sz" {
		t.Errorf("KeypadWords(\"79\") = %v, want 16 words from pw to sz", words)
	}
	if got := len(KeypadWords("234")); got != 27 {
		t.Errorf("len(KeypadWords(\"234\")) = %d, want 27", got)
	}
}
