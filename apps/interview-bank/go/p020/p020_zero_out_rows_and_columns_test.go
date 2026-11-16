package p020

import (
	"reflect"
	"testing"
)

func TestZeroOut(t *testing.T) {
	cases := []struct {
		grid [][]int
		want [][]int
	}{
		{[][]int{{1, 2, 3}, {4, 0, 6}, {7, 8, 9}}, [][]int{{1, 0, 3}, {0, 0, 0}, {7, 0, 9}}},
		{[][]int{{0, 1, 2, 0}, {3, 4, 5, 6}, {1, 3, 1, 5}}, [][]int{{0, 0, 0, 0}, {0, 4, 5, 0}, {0, 3, 1, 0}}},
		{[][]int{{0, 1}, {1, 1}}, [][]int{{0, 0}, {0, 1}}},
		{[][]int{{1, 2}, {3, 4}}, [][]int{{1, 2}, {3, 4}}},
		{[][]int{{0}}, [][]int{{0}}},
		{[][]int{{5, 0, 7}}, [][]int{{0, 0, 0}}},
	}
	for _, c := range cases {
		ZeroOut(c.grid)
		if !reflect.DeepEqual(c.grid, c.want) {
			t.Errorf("ZeroOut left %v, want %v", c.grid, c.want)
		}
	}
}
