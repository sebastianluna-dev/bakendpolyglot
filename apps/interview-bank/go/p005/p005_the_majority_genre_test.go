package p005

import "testing"

func TestMajorityGenre(t *testing.T) {
	cases := []struct {
		loans []string
		want  string
	}{
		{[]string{"mystery", "poetry", "mystery", "history", "mystery"}, "mystery"},
		{[]string{"poetry", "history", "poetry", "history"}, ""},
		{[]string{"drama", "comedy", "drama"}, "drama"},
		{[]string{"sci-fi"}, "sci-fi"},
		{[]string{}, ""},
	}
	for _, c := range cases {
		if got := MajorityGenre(c.loans); got != c.want {
			t.Errorf("MajorityGenre(%v) = %q, want %q", c.loans, got, c.want)
		}
	}
}
