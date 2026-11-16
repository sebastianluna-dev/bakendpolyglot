package p191

import (
	"slices"
	"testing"
)

func TestCountRequests(t *testing.T) {
	cases := []struct {
		lines []string
		want  []string
	}{
		{[]string{
			"2026-03-02T09:00:01Z GET /books 200 12",
			"2026-03-02T09:00:02Z POST /loans 201 40",
			"2026-03-02T09:00:03Z GET /books 200 9",
			"2026-03-02T09:00:04Z POST /loans 409 15",
			"2026-03-02T09:00:05Z GET /books 200 11",
			"2026-03-02T09:00:06Z POST /loans 201 38",
		}, []string{"GET /books 200 3", "POST /loans 201 2", "POST /loans 409 1"}},
		{[]string{
			"2026-03-02T09:01:00Z GET /members/7 404 3",
			"",
			"garbage",
			"2026-03-02T09:01:02Z DELETE /holds/9 204 5",
			"2026-03-02T09:01:03Z GET /members/7 200 4",
		}, []string{"DELETE /holds/9 204 1", "GET /members/7 200 1", "GET /members/7 404 1"}},
		{[]string{}, []string{}},
		{[]string{
			"2026-03-02T09:02:00Z GET  /books 200 7",
			"2026-03-02T09:02:01Z GET /books 200 7 ",
			"2026-03-02T09:02:02Z GET /books 200",
		}, []string{}},
		{[]string{"t1 GET /a 500 1", "t2 GET /b 200 1", "t3 GET /a 500 1", "t4 GET /b 200 1", "t5 GET /a 200 1"},
			[]string{"GET /a 500 2", "GET /b 200 2", "GET /a 200 1"}},
	}
	for _, c := range cases {
		if got := CountRequests(c.lines); !slices.Equal(got, c.want) {
			t.Errorf("CountRequests(%q) = %q, want %q", c.lines, got, c.want)
		}
	}
}
