package p173

import (
	"strings"
	"testing"
)

func TestCountDecodings(t *testing.T) {
	cases := []struct {
		digits string
		want   int
	}{
		{"2612", 4},
		{"301", 0},
		{"1201", 1},
		{"106", 1},
		{"0", 0},
		{"100", 0},
		{"27", 1},
		{"11106", 2},
		{strings.Repeat("1", 45), 1836311903},
	}
	for _, c := range cases {
		if got := CountDecodings(c.digits); got != c.want {
			t.Errorf("CountDecodings(%q) = %d, want %d", c.digits, got, c.want)
		}
	}
}
