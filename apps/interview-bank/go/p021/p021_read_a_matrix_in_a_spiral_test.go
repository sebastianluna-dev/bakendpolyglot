package p021

import (
	"slices"
	"testing"
)

func TestSpiralOrder(t *testing.T) {
	cases := []struct {
		grid [][]int
		want []int
	}{
		{[][]int{{1, 2, 3}, {8, 9, 4}, {7, 6, 5}}, []int{1, 2, 3, 4, 5, 6, 7, 8, 9}},
		{[][]int{{10, 11, 12, 13}, {20, 21, 22, 23}, {30, 31, 32, 33}}, []int{10, 11, 12, 13, 23, 33, 32, 31, 30, 20, 21, 22}},
		{[][]int{}, []int{}},
		{[][]int{{4, 5, 6}}, []int{4, 5, 6}},
		{[][]int{{4}, {5}, {6}}, []int{4, 5, 6}},
		{[][]int{{1, 2}, {3, 4}, {5, 6}}, []int{1, 2, 4, 6, 5, 3}},
	}
	for _, c := range cases {
		if got := SpiralOrder(c.grid); !slices.Equal(got, c.want) {
			t.Errorf("SpiralOrder(%v) = %v, want %v", c.grid, got, c.want)
		}
	}
}
