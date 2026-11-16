package p080

import "testing"

func TestCalculate(t *testing.T) {
	cases := []struct {
		s    string
		want int
	}{
		{"7 - (3 - (1 + 2))", 7},
		{"-(4 - (-2 + 9)) + 10", 13},
		{"12", 12},
		{" 1 + 2 ", 3},
		{"-5", -5},
		{"(((3)))", 3},
		{"10 - 2 - 3", 5},
		{"1 - (2 + 3)", -4},
		{"-(-(-1))", -1},
		{"2147483647", 2147483647},
	}
	for _, c := range cases {
		if got := Calculate(c.s); got != c.want {
			t.Errorf("Calculate(%q) = %d, want %d", c.s, got, c.want)
		}
	}
}
