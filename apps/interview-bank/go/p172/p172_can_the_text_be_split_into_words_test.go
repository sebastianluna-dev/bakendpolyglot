package p172

import "testing"

func TestCanSplit(t *testing.T) {
	cases := []struct {
		text  string
		words []string
		want  bool
	}{
		{"bookseller", []string{"books", "book", "seller"}, true},
		{"trucking", []string{"truck", "king", "in"}, false},
		{"", []string{"a"}, true},
		{"abcd", []string{}, false},
		{"seatseat", []string{"seat"}, true},
		{"aaaaaaa", []string{"aaaa", "aaa"}, true},
		{"aaaaaaaaab", []string{"a", "aa"}, false},
	}
	for _, c := range cases {
		if got := CanSplit(c.text, c.words); got != c.want {
			t.Errorf("CanSplit(%q, %v) = %v, want %v", c.text, c.words, got, c.want)
		}
	}
}
