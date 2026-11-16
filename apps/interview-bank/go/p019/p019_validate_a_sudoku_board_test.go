package p019

import (
	"slices"
	"testing"
)

func TestIsValidSudoku(t *testing.T) {
	board := []string{".9.5.3.46", "5.3.46...", "1........", "9.5.....2", "..1.6...5", "4..9....1", ".583...2.", "3.46.9758", ".2...8.1."}
	change := func(r int, row string) []string {
		copied := slices.Clone(board)
		copied[r] = row
		return copied
	}
	empty := make([]string, 9)
	for i := range empty {
		empty[i] = "........."
	}
	cases := []struct {
		name  string
		board []string
		want  bool
	}{
		{"valid", board, true},
		{"box only", change(2, "1.9......"), false},
		{"row only", change(0, "69.5.3.46"), false},
		{"column only", change(4, "3.1.6...5"), false},
		{"empty", empty, true},
	}
	for _, c := range cases {
		if got := IsValidSudoku(c.board); got != c.want {
			t.Errorf("%s: IsValidSudoku(%q) = %t, want %t", c.name, c.board, got, c.want)
		}
	}
}
