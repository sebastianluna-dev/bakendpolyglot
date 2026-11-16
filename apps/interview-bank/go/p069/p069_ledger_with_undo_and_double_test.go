package p069

import "testing"

func TestLedgerTotal(t *testing.T) {
	cases := []struct {
		commands []string
		want     int
	}{
		{[]string{"5", "-2", "D", "+", "U", "10", "+"}, 15},
		{[]string{"7", "U"}, 0},
		{[]string{}, 0},
		{[]string{"100"}, 100},
		{[]string{"2", "3", "+"}, 10},
		{[]string{"3", "D", "D", "U", "+"}, 18},
		{[]string{"-4", "D", "U", "U"}, 0},
	}
	for _, c := range cases {
		if got := LedgerTotal(c.commands); got != c.want {
			t.Errorf("LedgerTotal(%q) = %d, want %d", c.commands, got, c.want)
		}
	}
}
