package p053

import (
	"reflect"
	"testing"
)

func TestThreeToZero(t *testing.T) {
	cases := []struct {
		readings []int
		want     [][]int
	}{
		{[]int{-4, 1, 3, 0, -1, 2, -1}, [][]int{{-4, 1, 3}, {-1, -1, 2}, {-1, 0, 1}}},
		{[]int{0, 0, 0, 0}, [][]int{{0, 0, 0}}},
		{[]int{1, 2, -4}, [][]int{}},
		{[]int{}, [][]int{}},
		{[]int{5, -5}, [][]int{}},
		{[]int{2, -2, 0, 2, 0}, [][]int{{-2, 0, 2}}},
	}
	for _, c := range cases {
		if got := ThreeToZero(c.readings); !reflect.DeepEqual(got, c.want) {
			t.Errorf("ThreeToZero(%v) = %v, want %v", c.readings, got, c.want)
		}
	}
}
