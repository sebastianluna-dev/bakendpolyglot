package p022

import (
	"slices"
	"testing"
)

func TestRotateImage(t *testing.T) {
	cases := []struct {
		image [][]int
		want  [][]int
	}{
		{[][]int{{1, 2}, {3, 4}}, [][]int{{3, 1}, {4, 2}}},
		{[][]int{{11, 12, 13}, {21, 22, 23}, {31, 32, 33}}, [][]int{{31, 21, 11}, {32, 22, 12}, {33, 23, 13}}},
		{[][]int{}, [][]int{}},
		{[][]int{{7}}, [][]int{{7}}},
		{
			[][]int{{1, 2, 3, 4}, {5, 6, 7, 8}, {9, 10, 11, 12}, {13, 14, 15, 16}},
			[][]int{{13, 9, 5, 1}, {14, 10, 6, 2}, {15, 11, 7, 3}, {16, 12, 8, 4}},
		},
	}
	for _, c := range cases {
		RotateImage(c.image)
		if !slices.EqualFunc(c.image, c.want, slices.Equal[[]int]) {
			t.Errorf("RotateImage gave %v, want %v", c.image, c.want)
		}
	}
}
