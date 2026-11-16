package p156

import (
	"reflect"
	"testing"
)

func TestCombinationsToTarget(t *testing.T) {
	cases := []struct {
		candidates []int
		target     int
		want       [][]int
	}{
		{[]int{2, 3, 5}, 8, [][]int{{2, 2, 2, 2}, {2, 3, 3}, {3, 5}}},
		{[]int{7, 3, 2}, 7, [][]int{{2, 2, 3}, {7}}},
		{[]int{4, 6}, 5, [][]int{}},
		{[]int{1}, 3, [][]int{{1, 1, 1}}},
		{[]int{9}, 9, [][]int{{9}}},
		{[]int{5, 1}, 6, [][]int{{1, 1, 1, 1, 1, 1}, {1, 5}}},
	}
	for _, c := range cases {
		if got := CombinationsToTarget(c.candidates, c.target); !reflect.DeepEqual(got, c.want) {
			t.Errorf("CombinationsToTarget(%v, %d) = %v, want %v", c.candidates, c.target, got, c.want)
		}
	}
}
