package p100

import "testing"

func TestConfigHistory(t *testing.T) {
	expect := func(name, got, want string) {
		t.Helper()
		if got != want {
			t.Errorf("%s = %q, want %q", name, got, want)
		}
	}

	h := NewConfigHistory()
	expect(`Get("hold.minutes", 100) before any Set`, h.Get("hold.minutes", 100), "")
	h.Set("hold.minutes", "10", 100)
	h.Set("hold.minutes", "15", 250)
	h.Set("currency", "EUR", 120)
	expect(`Get("hold.minutes", 99)`, h.Get("hold.minutes", 99), "")
	expect(`Get("hold.minutes", 100)`, h.Get("hold.minutes", 100), "10")
	expect(`Get("hold.minutes", 249)`, h.Get("hold.minutes", 249), "10")
	expect(`Get("hold.minutes", 250)`, h.Get("hold.minutes", 250), "15")
	expect(`Get("hold.minutes", 1000)`, h.Get("hold.minutes", 1000), "15")
	expect(`Get("currency", 119)`, h.Get("currency", 119), "")
	expect(`Get("currency", 500)`, h.Get("currency", 500), "EUR")
	expect(`Get("refunds", 500)`, h.Get("refunds", 500), "")
	h.Set("hold.minutes", "20", 400)
	expect(`Get("hold.minutes", 399)`, h.Get("hold.minutes", 399), "15")
	expect(`Get("hold.minutes", 400)`, h.Get("hold.minutes", 400), "20")
}
