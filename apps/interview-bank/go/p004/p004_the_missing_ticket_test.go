package p004

import "testing"

func TestMissingTicket(t *testing.T) {
	many := make([]int, 0, 69999)
	for ticket := 1; ticket <= 70000; ticket++ {
		if ticket != 31415 {
			many = append(many, ticket)
		}
	}
	cases := []struct {
		scanned []int
		want    int
	}{
		{[]int{3, 1, 4, 5}, 2},
		{[]int{2}, 1},
		{[]int{1}, 2},
		{[]int{}, 1},
		{many, 31415},
	}
	for _, c := range cases {
		if got := MissingTicket(c.scanned); got != c.want {
			t.Errorf("MissingTicket(%d tickets) = %d, want %d", len(c.scanned), got, c.want)
		}
	}
}
