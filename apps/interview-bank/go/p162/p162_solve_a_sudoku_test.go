package p162

import (
	"slices"
	"testing"
)

func TestSolveSudoku(t *testing.T) {
	puzzle := []string{"..59..386", ".86....29", "1....3...", ".51...8..", "8.4..7...", "...46...1", ".12....4.", "..87..5..", "647.....8"}
	solved := []string{"475921386", "386574129", "129683475", "751392864", "864157293", "293468751", "512839647", "938746512", "647215938"}
	stuck := []string{"12345678.", "........9", ".........", ".........", ".........", ".........", ".........", ".........", "........."}
	easy := []string{".1.52.3..", "2.57..1.6", "43...1...", "39.21..48", "......5.1", "15...7963", "76..9.48.", "5.8.7.21.", "..1...637"}
	easySolved := []string{"619528374", "285743196", "437961852", "396215748", "874639521", "152487963", "763192485", "548376219", "921854637"}
	cases := []struct {
		name  string
		board []string
		want  []string
	}{
		{"puzzle", puzzle, solved},
		{"no solution", stuck, []string{}},
		{"already solved", solved, solved},
		{"easy", easy, easySolved},
	}
	for _, c := range cases {
		if got := SolveSudoku(c.board); !slices.Equal(got, c.want) {
			t.Errorf("%s: SolveSudoku = %v, want %v", c.name, got, c.want)
		}
	}
}
