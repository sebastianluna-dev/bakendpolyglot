package p076

import (
	"slices"
	"testing"
)

func TestSurvivingParcels(t *testing.T) {
	cases := []struct {
		parcels []int
		want    []int
	}{
		{[]int{4, 7, -5}, []int{4, 7}},
		{[]int{3, 6, -6, -8, 2}, []int{-8, 2}},
		{[]int{}, []int{}},
		{[]int{-3, 4}, []int{-3, 4}},
		{[]int{5, -5}, []int{}},
		{[]int{1, -2, -2, -2}, []int{-2, -2, -2}},
		{[]int{10, 2, -5}, []int{10}},
	}
	for _, c := range cases {
		if got := SurvivingParcels(c.parcels); !slices.Equal(got, c.want) {
			t.Errorf("SurvivingParcels(%v) = %v, want %v", c.parcels, got, c.want)
		}
	}
}
