package p154

import (
	"reflect"
	"testing"
)

func TestAllSubsets(t *testing.T) {
	cases := []struct {
		nums []int
		want [][]int
	}{
		{[]int{1, 2, 3}, [][]int{{}, {1}, {1, 2}, {1, 2, 3}, {1, 3}, {2}, {2, 3}, {3}}},
		{[]int{4, 1}, [][]int{{}, {4}, {4, 1}, {1}}},
		{[]int{}, [][]int{{}}},
		{[]int{7}, [][]int{{}, {7}}},
		{[]int{-1, 0, 2}, [][]int{{}, {-1}, {-1, 0}, {-1, 0, 2}, {-1, 2}, {0}, {0, 2}, {2}}},
	}
	for _, c := range cases {
		if got := AllSubsets(c.nums); !reflect.DeepEqual(got, c.want) {
			t.Errorf("AllSubsets(%v) = %v, want %v", c.nums, got, c.want)
		}
	}
	if got := len(AllSubsets([]int{0, 1, 2, 3, 4, 5, 6, 7, 8, 9})); got != 1024 {
		t.Errorf("len(AllSubsets(0..9)) = %d, want 1024", got)
	}
}
