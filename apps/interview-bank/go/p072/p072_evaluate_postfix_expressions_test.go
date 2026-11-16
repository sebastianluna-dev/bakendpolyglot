package p072

import "testing"

func TestEvaluatePostfix(t *testing.T) {
	cases := []struct {
		tokens []string
		want   int
	}{
		{[]string{"4", "13", "5", "/", "+"}, 6},
		{[]string{"-7", "2", "/", "3", "*"}, -9},
		{[]string{"42"}, 42},
		{[]string{"8", "2", "/", "3", "-"}, 1},
		{[]string{"2", "-5", "/"}, 0},
		{[]string{"-9", "-2", "/"}, 4},
		{[]string{"5", "1", "2", "+", "4", "*", "+", "3", "-"}, 14},
	}
	for _, c := range cases {
		if got := EvaluatePostfix(c.tokens); got != c.want {
			t.Errorf("EvaluatePostfix(%q) = %d, want %d", c.tokens, got, c.want)
		}
	}
}
