package p028

import "testing"

func TestLongestSharedPrefix(t *testing.T) {
	cases := []struct {
		words []string
		want  string
	}{
		{[]string{"QA76.73", "QA76.76", "QA76.9"}, "QA76."},
		{[]string{"parcel", "pulse", "seat"}, ""},
		{[]string{}, ""},
		{[]string{"solo"}, "solo"},
		{[]string{"ab", "a"}, "a"},
		{[]string{"", "x"}, ""},
		{[]string{"hold", "hold"}, "hold"},
	}
	for _, c := range cases {
		if got := LongestSharedPrefix(c.words); got != c.want {
			t.Errorf("LongestSharedPrefix(%q) = %q, want %q", c.words, got, c.want)
		}
	}
}
