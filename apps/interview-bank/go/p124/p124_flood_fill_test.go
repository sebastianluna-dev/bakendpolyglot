package p124

import (
	"reflect"
	"testing"
)

func TestFloodFill(t *testing.T) {
	cases := []struct {
		image           [][]int
		row, col, color int
		want            [][]int
	}{
		{[][]int{{1, 1, 0}, {0, 1, 0}, {1, 0, 1}}, 0, 0, 3, [][]int{{3, 3, 0}, {0, 3, 0}, {1, 0, 1}}},
		{[][]int{{4, 4}, {4, 4}}, 1, 1, 4, [][]int{{4, 4}, {4, 4}}},
		{[][]int{{0}}, 0, 0, 7, [][]int{{7}}},
		{[][]int{{1, 2, 1}, {1, 2, 1}, {1, 1, 1}}, 0, 2, 9, [][]int{{9, 2, 9}, {9, 2, 9}, {9, 9, 9}}},
		{[][]int{{5, 5, 6}, {6, 5, 6}}, 1, 2, 0, [][]int{{5, 5, 0}, {6, 5, 0}}},
	}
	for _, c := range cases {
		if got := FloodFill(c.image, c.row, c.col, c.color); !reflect.DeepEqual(got, c.want) {
			t.Errorf("FloodFill(..., %d, %d, %d) = %v, want %v", c.row, c.col, c.color, got, c.want)
		}
	}
}
