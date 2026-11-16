package p027

import "testing"

func TestFirstUniqueIndex(t *testing.T) {
	cases := []struct {
		word string
		want int
	}{
		{"shelfwise", 1},
		{"noon", -1},
		{"", -1},
		{"z", 0},
		{"aabbc", 4},
		{"pear", 0},
	}
	for _, c := range cases {
		if got := FirstUniqueIndex(c.word); got != c.want {
			t.Errorf("FirstUniqueIndex(%q) = %d, want %d", c.word, got, c.want)
		}
	}
}
