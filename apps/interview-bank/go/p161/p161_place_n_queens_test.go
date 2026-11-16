package p161

import (
	"reflect"
	"slices"
	"testing"
)

func TestPlaceQueens(t *testing.T) {
	cases := []struct {
		n    int
		want [][]string
	}{
		{4, [][]string{{".Q..", "...Q", "Q...", "..Q."}, {"..Q.", "Q...", "...Q", ".Q.."}}},
		{1, [][]string{{"Q"}}},
		{2, [][]string{}},
		{3, [][]string{}},
	}
	for _, c := range cases {
		if got := PlaceQueens(c.n); !reflect.DeepEqual(got, c.want) {
			t.Errorf("PlaceQueens(%d) = %v, want %v", c.n, got, c.want)
		}
	}
	if got := len(PlaceQueens(6)); got != 4 {
		t.Errorf("len(PlaceQueens(6)) = %d, want 4", got)
	}
	eight := PlaceQueens(8)
	first := []string{"Q.......", "....Q...", ".......Q", ".....Q..", "..Q.....", "......Q.", ".Q......", "...Q...."}
	if len(eight) != 92 || !slices.Equal(eight[0], first) {
		t.Errorf("PlaceQueens(8): %d boards, first %v; want 92, first %v", len(eight), eight[0], first)
	}
}
