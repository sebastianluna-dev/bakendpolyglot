package p137

import "testing"

func TestShortestChain(t *testing.T) {
	cases := []struct {
		start, target string
		words         []string
		want          int
	}{
		{"cold", "warm", []string{"cord", "card", "ward", "warm", "worm", "word", "wore"}, 5},
		{"lead", "gold", []string{"load", "goad"}, 0},
		{"cat", "cot", []string{"cot"}, 2},
		{"cat", "dog", []string{}, 0},
		{"hit", "hot", []string{"hot", "hot", "hit"}, 2},
		{"mine", "bond", []string{"mind", "bind", "bond", "mend"}, 4},
		{"a", "c", []string{"b", "c"}, 2},
	}
	for _, c := range cases {
		if got := ShortestChain(c.start, c.target, c.words); got != c.want {
			t.Errorf("ShortestChain(%q, %q, %v) = %d, want %d", c.start, c.target, c.words, got, c.want)
		}
	}
}
