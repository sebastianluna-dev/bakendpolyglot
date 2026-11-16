package p092

import "testing"

func TestInsertPosition(t *testing.T) {
	stops := []int{480, 540, 600, 720}
	cases := []struct {
		deadlines []int
		deadline  int
		want      int
	}{
		{stops, 610, 3},
		{[]int{480, 540, 540, 600}, 540, 1},
		{[]int{}, 300, 0},
		{stops, 100, 0},
		{stops, 800, 4},
		{stops, 480, 0},
		{stops, 720, 3},
		{[]int{5, 5, 5}, 5, 0},
	}
	for _, c := range cases {
		if got := InsertPosition(c.deadlines, c.deadline); got != c.want {
			t.Errorf("InsertPosition(%v, %d) = %d, want %d", c.deadlines, c.deadline, got, c.want)
		}
	}
}
