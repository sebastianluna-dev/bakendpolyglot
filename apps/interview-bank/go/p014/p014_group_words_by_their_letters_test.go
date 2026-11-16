package p014

import (
	"reflect"
	"testing"
)

func TestGroupByLetters(t *testing.T) {
	cases := []struct {
		words []string
		want  [][]string
	}{
		{
			[]string{"listen", "stone", "silent", "notes", "enlist", "tones", "tab"},
			[][]string{{"listen", "silent", "enlist"}, {"stone", "notes", "tones"}, {"tab"}},
		},
		{[]string{"loop", "pool", "polo", "lop"}, [][]string{{"loop", "pool", "polo"}, {"lop"}}},
		{[]string{}, [][]string{}},
		{[]string{"ab", "ba", "ab"}, [][]string{{"ab", "ba", "ab"}}},
		{[]string{"", "a", ""}, [][]string{{"", ""}, {"a"}}},
	}
	for _, c := range cases {
		if got := GroupByLetters(c.words); !reflect.DeepEqual(got, c.want) {
			t.Errorf("GroupByLetters(%q) = %q, want %q", c.words, got, c.want)
		}
	}
}
