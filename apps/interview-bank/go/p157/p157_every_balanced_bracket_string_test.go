package p157

import (
	"slices"
	"testing"
)

func TestBalancedBrackets(t *testing.T) {
	cases := []struct {
		n    int
		want []string
	}{
		{3, []string{"((()))", "(()())", "(())()", "()(())", "()()()"}},
		{1, []string{"()"}},
		{0, []string{""}},
		{2, []string{"(())", "()()"}},
	}
	for _, c := range cases {
		if got := BalancedBrackets(c.n); !slices.Equal(got, c.want) {
			t.Errorf("BalancedBrackets(%d) = %v, want %v", c.n, got, c.want)
		}
	}
	if got := len(BalancedBrackets(4)); got != 14 {
		t.Errorf("len(BalancedBrackets(4)) = %d, want 14", got)
	}
}
