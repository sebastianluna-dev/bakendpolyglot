package p048

import (
	"slices"
	"testing"
)

func TestRemoveDuplicates(t *testing.T) {
	cases := []struct {
		ids  []int
		want []int
	}{
		{[]int{3, 3, 5, 8, 8, 8, 9}, []int{3, 5, 8, 9}},
		{[]int{7, 7, 7}, []int{7}},
		{[]int{}, []int{}},
		{[]int{1, 2, 3}, []int{1, 2, 3}},
		{[]int{10, 10, 20, 20, 30}, []int{10, 20, 30}},
	}
	for _, c := range cases {
		k := RemoveDuplicates(c.ids)
		if got := c.ids[:k]; !slices.Equal(got, c.want) {
			t.Errorf("RemoveDuplicates kept %v, want %v", got, c.want)
		}
	}
}
