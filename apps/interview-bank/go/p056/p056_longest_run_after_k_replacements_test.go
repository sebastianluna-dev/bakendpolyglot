package p056

import "testing"

func TestLongestRunAfterReplacements(t *testing.T) {
	cases := []struct {
		s    string
		k    int
		want int
	}{
		{"ABAACBA", 1, 4},
		{"XYXY", 2, 4},
		{"", 0, 0},
		{"AAAA", 0, 4},
		{"ABCD", 0, 1},
		{"AB", 5, 2},
		{"BAAABBBBA", 2, 6},
	}
	for _, c := range cases {
		if got := LongestRunAfterReplacements(c.s, c.k); got != c.want {
			t.Errorf("LongestRunAfterReplacements(%q, %d) = %d, want %d", c.s, c.k, got, c.want)
		}
	}
}
