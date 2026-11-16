package p091

import "testing"

func TestBinarySearch(t *testing.T) {
	ids := []int{3, 8, 15, 21, 40, 57}
	cases := []struct {
		ids    []int
		target int
		want   int
	}{
		{ids, 21, 3},
		{ids, 10, -1},
		{[]int{}, 5, -1},
		{[]int{7}, 7, 0},
		{ids, 3, 0},
		{ids, 57, 5},
		{ids, 1, -1},
		{ids, 99, -1},
	}
	for _, c := range cases {
		if got := BinarySearch(c.ids, c.target); got != c.want {
			t.Errorf("BinarySearch(%v, %d) = %d, want %d", c.ids, c.target, got, c.want)
		}
	}
}
