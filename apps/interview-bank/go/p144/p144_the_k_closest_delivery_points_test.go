package p144

import (
	"reflect"
	"testing"
)

func TestKClosest(t *testing.T) {
	cases := []struct {
		points [][]int
		k      int
		want   [][]int
	}{
		{[][]int{{3, 4}, {1, -2}, {-2, 1}, {0, 5}, {2, 2}}, 3, [][]int{{-2, 1}, {1, -2}, {2, 2}}},
		{[][]int{{2, 0}, {0, 2}, {0, -2}, {-2, 0}}, 2, [][]int{{-2, 0}, {0, -2}}},
		{[][]int{{5, 5}, {1, 1}}, 0, [][]int{}},
		{[][]int{{6, 1}, {-1, -1}, {0, 0}}, 3, [][]int{{0, 0}, {-1, -1}, {6, 1}}},
		{[][]int{{1, 1}, {0, 3}, {1, 1}}, 2, [][]int{{1, 1}, {1, 1}}},
		{[][]int{{10000, -10000}, {-10000, 10000}}, 1, [][]int{{-10000, 10000}}},
	}
	for _, c := range cases {
		if got := KClosest(c.points, c.k); !reflect.DeepEqual(got, c.want) {
			t.Errorf("KClosest(%v, %d) = %v, want %v", c.points, c.k, got, c.want)
		}
	}
}
