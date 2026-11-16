package p101

import "testing"

func TestMedianOfTwoSorted(t *testing.T) {
	cases := []struct {
		a, b []int
		want float64
	}{
		{[]int{12, 18, 31}, []int{9, 20, 25, 40}, 20},
		{[]int{5, 9}, []int{6, 30}, 7.5},
		{[]int{}, []int{4}, 4},
		{[]int{1, 2}, []int{}, 1.5},
		{[]int{2, 2, 2}, []int{2, 2}, 2},
		{[]int{1, 2}, []int{3, 4, 5, 6}, 3.5},
		{[]int{1, 3, 5, 7, 9}, []int{2}, 4},
		{[]int{-5, -1}, []int{-3}, -3},
		{[]int{10}, []int{1, 2, 3}, 2.5},
	}
	for _, c := range cases {
		if got := MedianOfTwoSorted(c.a, c.b); got != c.want {
			t.Errorf("MedianOfTwoSorted(%v, %v) = %v, want %v", c.a, c.b, got, c.want)
		}
	}
}
