package p145

import "testing"

func TestScheduleTime(t *testing.T) {
	cases := []struct {
		tasks    string
		cooldown int
		want     int
	}{
		{"AAABBC", 2, 7},
		{"AABBCD", 1, 6},
		{"AAABBB", 2, 8},
		{"AAAA", 3, 13},
		{"ABAB", 0, 4},
		{"A", 5, 1},
		{"AAABCDEFG", 2, 9},
	}
	for _, c := range cases {
		if got := ScheduleTime(c.tasks, c.cooldown); got != c.want {
			t.Errorf("ScheduleTime(%q, %d) = %d, want %d", c.tasks, c.cooldown, got, c.want)
		}
	}
}
