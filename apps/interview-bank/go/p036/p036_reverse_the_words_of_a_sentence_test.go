package p036

import "testing"

func TestReverseWords(t *testing.T) {
	cases := []struct {
		sentence string
		want     string
	}{
		{"seat 14 is free", "free is 14 seat"},
		{"  return   the copy ", "copy the return"},
		{"hold", "hold"},
		{"", ""},
		{"   ", ""},
		{"a b c", "c b a"},
	}
	for _, c := range cases {
		if got := ReverseWords(c.sentence); got != c.want {
			t.Errorf("ReverseWords(%q) = %q, want %q", c.sentence, got, c.want)
		}
	}
}
