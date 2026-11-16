package p009

import (
	"slices"
	"testing"
)

func TestRotateCarousel(t *testing.T) {
	cases := []struct {
		slides []int
		k      int
		want   []int
	}{
		{[]int{1, 2, 3, 4, 5}, 2, []int{4, 5, 1, 2, 3}},
		{[]int{10, 20}, 5, []int{20, 10}},
		{[]int{1, 2, 3}, 0, []int{1, 2, 3}},
		{[]int{1, 2, 3}, 3, []int{1, 2, 3}},
		{[]int{7}, 4, []int{7}},
		{[]int{}, 3, []int{}},
	}
	for _, c := range cases {
		got := slices.Clone(c.slides)
		RotateCarousel(got, c.k)
		if !slices.Equal(got, c.want) {
			t.Errorf("RotateCarousel(%v, %d) gives %v, want %v", c.slides, c.k, got, c.want)
		}
	}
}
