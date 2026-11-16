package p155

import (
	"reflect"
	"testing"
)

func TestAllOrderings(t *testing.T) {
	cases := []struct {
		nums []int
		want [][]int
	}{
		{[]int{1, 2, 3}, [][]int{{1, 2, 3}, {1, 3, 2}, {2, 1, 3}, {2, 3, 1}, {3, 1, 2}, {3, 2, 1}}},
		{[]int{3, 1, 2}, [][]int{{3, 1, 2}, {3, 2, 1}, {1, 3, 2}, {1, 2, 3}, {2, 3, 1}, {2, 1, 3}}},
		{[]int{}, [][]int{{}}},
		{[]int{9}, [][]int{{9}}},
		{[]int{5, -5}, [][]int{{5, -5}, {-5, 5}}},
	}
	for _, c := range cases {
		if got := AllOrderings(c.nums); !reflect.DeepEqual(got, c.want) {
			t.Errorf("AllOrderings(%v) = %v, want %v", c.nums, got, c.want)
		}
	}
	if got := len(AllOrderings([]int{1, 2, 3, 4, 5, 6})); got != 720 {
		t.Errorf("len(AllOrderings(1..6)) = %d, want 720", got)
	}
}
