package p043

import "testing"

func TestSayWhatYouSee(t *testing.T) {
	cases := []struct {
		start  string
		rounds int
		want   string
	}{
		{"1", 4, "111221"},
		{"3322251", 1, "23321511"},
		{"1", 0, "1"},
		{"22", 5, "22"},
		{"1111111111", 1, "101"},
		{"1", 9, "13211311123113112211"},
	}
	for _, c := range cases {
		if got := SayWhatYouSee(c.start, c.rounds); got != c.want {
			t.Errorf("SayWhatYouSee(%q, %d) = %q, want %q", c.start, c.rounds, got, c.want)
		}
	}
}
