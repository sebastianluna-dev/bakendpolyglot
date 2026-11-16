package p044

import (
	"slices"
	"testing"
)

func TestJustifyText(t *testing.T) {
	cases := []struct {
		words []string
		width int
		want  []string
	}{
		{[]string{"Your", "loan", "of", "Dune", "is", "due", "on", "Friday."}, 16,
			[]string{"Your   loan   of", "Dune  is  due on", "Friday.         "}},
		{[]string{"Hold", "expires", "in", "ten", "minutes"}, 10,
			[]string{"Hold      ", "expires in", "ten       ", "minutes   "}},
		{[]string{"to", "be", "or", "not"}, 6, []string{"to  be", "or not"}},
		{[]string{"exact"}, 5, []string{"exact"}},
		{[]string{}, 8, []string{}},
	}
	for _, c := range cases {
		if got := JustifyText(c.words, c.width); !slices.Equal(got, c.want) {
			t.Errorf("JustifyText(%q, %d) = %q, want %q", c.words, c.width, got, c.want)
		}
	}
}
