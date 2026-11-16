package p007

import (
	"slices"
	"testing"
)

func TestAddOne(t *testing.T) {
	cases := []struct {
		digits []int
		want   []int
	}{
		{[]int{1, 2, 9}, []int{1, 3, 0}},
		{[]int{9, 9, 9}, []int{1, 0, 0, 0}},
		{[]int{4, 5}, []int{4, 6}},
		{[]int{0}, []int{1}},
		{slices.Repeat([]int{9}, 20), append([]int{1}, make([]int, 20)...)},
	}
	for _, c := range cases {
		if got := AddOne(c.digits); !slices.Equal(got, c.want) {
			t.Errorf("AddOne(%v) = %v, want %v", c.digits, got, c.want)
		}
	}
}

func TestAddOneKeepsInput(t *testing.T) {
	input := []int{1, 2, 9}
	AddOne(input)
	if !slices.Equal(input, []int{1, 2, 9}) {
		t.Errorf("AddOne changed its input to %v", input)
	}
}
