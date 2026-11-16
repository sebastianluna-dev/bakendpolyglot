package p198

import (
	"fmt"
	"testing"
)

func TestIdempotentHandler(t *testing.T) {
	runs := 0
	h := NewIdempotentHandler(func(body string) APIResponse {
		runs++
		if body == "hold=3" {
			return APIResponse{409, "seat already sold"}
		}
		return APIResponse{201, fmt.Sprintf("order %d", runs)}
	})
	steps := []struct {
		key, body string
		want      APIResponse
		wantRuns  int
	}{
		{"k1", "hold=7", APIResponse{201, "order 1"}, 1},
		{"k1", "hold=7", APIResponse{201, "order 1"}, 1},
		{"k2", "hold=9", APIResponse{201, "order 2"}, 2},
		{"k1", "hold=8", Conflict, 2},
		{"k1", "hold=7", APIResponse{201, "order 1"}, 2},
		{"k3", "hold=3", APIResponse{409, "seat already sold"}, 3},
		{"k3", "hold=3", APIResponse{409, "seat already sold"}, 3},
		{"k4", "hold=7", APIResponse{201, "order 4"}, 4},
	}
	for _, s := range steps {
		if got := h.Handle(s.key, s.body); got != s.want || runs != s.wantRuns {
			t.Errorf("Handle(%q, %q) = %v after %d runs, want %v after %d", s.key, s.body, got, runs, s.want, s.wantRuns)
		}
	}
}
