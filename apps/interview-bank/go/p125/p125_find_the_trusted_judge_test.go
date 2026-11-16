package p125

import "testing"

func TestFindJudge(t *testing.T) {
	cases := []struct {
		n     int
		trust [][]int
		want  int
	}{
		{4, [][]int{{1, 3}, {2, 3}, {4, 3}, {4, 1}}, 3},
		{3, [][]int{{1, 3}, {2, 3}, {3, 1}}, -1},
		{1, [][]int{}, 1},
		{2, [][]int{}, -1},
		{2, [][]int{{1, 2}, {2, 1}}, -1},
		{3, [][]int{{1, 2}, {2, 3}}, -1},
	}
	for _, c := range cases {
		if got := FindJudge(c.n, c.trust); got != c.want {
			t.Errorf("FindJudge(%d, %v) = %d, want %d", c.n, c.trust, got, c.want)
		}
	}
}
