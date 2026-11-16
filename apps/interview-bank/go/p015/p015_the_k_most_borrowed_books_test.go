package p015

import (
	"slices"
	"testing"
)

func TestMostBorrowed(t *testing.T) {
	cases := []struct {
		loans []string
		k     int
		want  []string
	}{
		{[]string{"dune", "emma", "dune", "ulysses", "emma", "dune", "beloved"}, 2, []string{"dune", "emma"}},
		{[]string{"emma", "beloved", "ulysses", "beloved", "emma"}, 2, []string{"beloved", "emma"}},
		{[]string{"dune"}, 3, []string{"dune"}},
		{[]string{}, 1, []string{}},
		{[]string{"c", "b", "a"}, 2, []string{"a", "b"}},
	}
	for _, c := range cases {
		if got := MostBorrowed(c.loans, c.k); !slices.Equal(got, c.want) {
			t.Errorf("MostBorrowed(%q, %d) = %q, want %q", c.loans, c.k, got, c.want)
		}
	}
}
