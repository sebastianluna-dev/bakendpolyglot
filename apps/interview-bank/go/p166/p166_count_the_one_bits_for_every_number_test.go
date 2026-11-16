package p166

import (
	"slices"
	"testing"
)

func TestOneBitCounts(t *testing.T) {
	cases := []struct {
		n    int
		want []int
	}{
		{5, []int{0, 1, 1, 2, 1, 2}},
		{0, []int{0}},
		{1, []int{0, 1}},
		{8, []int{0, 1, 1, 2, 1, 2, 2, 3, 1}},
	}
	for _, c := range cases {
		if got := OneBitCounts(c.n); !slices.Equal(got, c.want) {
			t.Errorf("OneBitCounts(%d) = %v, want %v", c.n, got, c.want)
		}
	}
	if got := OneBitCounts(1023)[1023]; got != 10 {
		t.Errorf("OneBitCounts(1023)[1023] = %d, want 10", got)
	}
	if got := OneBitCounts(100000)[65535]; got != 16 {
		t.Errorf("OneBitCounts(100000)[65535] = %d, want 16", got)
	}
}
