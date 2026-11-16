package p001

import "testing"

func TestHasDuplicate(t *testing.T) {
	cases := []struct {
		barcodes []string
		want     bool
	}{
		{[]string{"SW-1042", "SW-0007", "SW-2210", "SW-0007"}, true},
		{[]string{"SW-1042", "SW-0007", "SW-2210"}, false},
		{[]string{"SW-0007", "sw-0007"}, false},
		{[]string{"SW-1042"}, false},
		{[]string{}, false},
	}
	for _, c := range cases {
		if got := HasDuplicate(c.barcodes); got != c.want {
			t.Errorf("HasDuplicate(%v) = %v, want %v", c.barcodes, got, c.want)
		}
	}
}
