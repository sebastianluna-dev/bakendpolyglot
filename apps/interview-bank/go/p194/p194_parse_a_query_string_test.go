package p194

import (
	"maps"
	"slices"
	"testing"
)

func TestParseQuery(t *testing.T) {
	cases := []struct {
		query string
		want  map[string][]string
	}{
		{"genre=sci-fi&sort=title&genre=history", map[string][]string{"genre": {"sci-fi", "history"}, "sort": {"title"}}},
		{"q=war+%26+peace&note=50%25&empty=&flag",
			map[string][]string{"q": {"war & peace"}, "note": {"50%"}, "empty": {""}, "flag": {""}}},
		{"", map[string][]string{}},
		{"a=1&&b=2&", map[string][]string{"a": {"1"}, "b": {"2"}}},
		{"=5&k=v", map[string][]string{"k": {"v"}}},
		{"expr=a=b", map[string][]string{"expr": {"a=b"}}},
		{"name=Jos%C3%A9&price=%E2%82%AC5", map[string][]string{"name": {"José"}, "price": {"€5"}}},
		{"bad=%zz%4&tail=%", map[string][]string{"bad": {"%zz%4"}, "tail": {"%"}}},
		{"sort%5Bby%5D=title&path=%2fa%2F", map[string][]string{"sort[by]": {"title"}, "path": {"/a/"}}},
		{"plus=%2B+", map[string][]string{"plus": {"+ "}}},
	}
	for _, c := range cases {
		if got := ParseQuery(c.query); !maps.EqualFunc(got, c.want, slices.Equal) {
			t.Errorf("ParseQuery(%q) = %q, want %q", c.query, got, c.want)
		}
	}
}
