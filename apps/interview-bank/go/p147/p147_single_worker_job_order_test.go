package p147

import (
	"slices"
	"testing"
)

func TestJobOrder(t *testing.T) {
	cases := []struct {
		jobs [][]int
		want []int
	}{
		{[][]int{{1, 3}, {2, 1}, {3, 2}, {4, 1}}, []int{0, 1, 3, 2}},
		{[][]int{{5, 2}, {5, 2}, {5, 1}, {20, 1}}, []int{2, 0, 1, 3}},
		{[][]int{{7, 4}}, []int{0}},
		{[][]int{{1, 2}, {3, 5}, {3, 1}}, []int{0, 2, 1}},
		{[][]int{{9, 1}, {1, 1}}, []int{1, 0}},
		{[][]int{{1, 1000000000}, {1, 1000000000}, {1000000000, 1}}, []int{0, 2, 1}},
	}
	for _, c := range cases {
		if got := JobOrder(c.jobs); !slices.Equal(got, c.want) {
			t.Errorf("JobOrder(%v) = %v, want %v", c.jobs, got, c.want)
		}
	}
}
