package p143

import (
	"slices"
	"testing"
)

func TestMedalRanks(t *testing.T) {
	cases := []struct {
		scores []int
		want   []string
	}{
		{[]int{88, 95, 70, 91}, []string{"Bronze Medal", "Gold Medal", "4", "Silver Medal"}},
		{[]int{3, 9, 1, 12, 5, 7}, []string{"5", "Silver Medal", "6", "Gold Medal", "4", "Bronze Medal"}},
		{[]int{42}, []string{"Gold Medal"}},
		{[]int{0, 5}, []string{"Silver Medal", "Gold Medal"}},
		{[]int{}, []string{}},
		{
			[]int{10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110},
			[]string{"11", "10", "9", "8", "7", "6", "5", "4", "Bronze Medal", "Silver Medal", "Gold Medal"},
		},
	}
	for _, c := range cases {
		if got := MedalRanks(c.scores); !slices.Equal(got, c.want) {
			t.Errorf("MedalRanks(%v) = %v, want %v", c.scores, got, c.want)
		}
	}
}
