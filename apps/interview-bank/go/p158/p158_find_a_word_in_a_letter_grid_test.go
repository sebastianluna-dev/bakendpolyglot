package p158

import "testing"

func TestFindWord(t *testing.T) {
	grid := []string{"she", "afl", "nao"}
	cases := []struct {
		grid []string
		word string
		want bool
	}{
		{grid, "shelf", true},
		{grid, "lol", false},
		{grid, "loan", true},
		{grid, "fan", true},
		{grid, "sf", false},
		{grid, "o", true},
		{grid, "z", false},
		{grid, "shelfaloans", false},
		{[]string{"aa", "ab"}, "aaab", true},
	}
	for _, c := range cases {
		if got := FindWord(c.grid, c.word); got != c.want {
			t.Errorf("FindWord(%v, %q) = %t, want %t", c.grid, c.word, got, c.want)
		}
	}
}
