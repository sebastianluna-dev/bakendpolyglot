package p193

import (
	"slices"
	"testing"
)

func ptr(v int) *int { return &v }

func TestFetchPage(t *testing.T) {
	ids := []int{3, 8, 15, 21, 30, 42, 57}
	cases := []struct {
		ids   []int
		after *int
		limit int
		items []int
		next  *int
	}{
		{ids, nil, 3, []int{3, 8, 15}, ptr(15)},
		{ids, ptr(15), 3, []int{21, 30, 42}, ptr(42)},
		{ids, ptr(42), 3, []int{57}, nil},
		{ids, ptr(20), 3, []int{21, 30, 42}, ptr(42)},
		{ids, ptr(57), 3, []int{}, nil},
		{ids, ptr(1), 10, []int{3, 8, 15, 21, 30, 42, 57}, nil},
		{[]int{3, 8, 15, 21, 30, 42}, ptr(15), 3, []int{21, 30, 42}, nil},
		{[]int{}, nil, 5, []int{}, nil},
	}
	for _, c := range cases {
		got := FetchPage(c.ids, c.after, c.limit)
		sameNext := (got.Next == nil) == (c.next == nil) && (got.Next == nil || *got.Next == *c.next)
		if !slices.Equal(got.Items, c.items) || !sameNext {
			t.Errorf("FetchPage(%v, %v, %d) = %v, %v; want %v, %v", c.ids, c.after, c.limit, got.Items, got.Next, c.items, c.next)
		}
	}
}
