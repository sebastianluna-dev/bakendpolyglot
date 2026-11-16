package p136

import (
	"math"
	"testing"
)

func near(got, want []float64) bool {
	if len(got) != len(want) {
		return false
	}
	for i := range got {
		if math.Abs(got[i]-want[i]) >= 1e-9 {
			return false
		}
	}
	return true
}

func TestExchangeRates(t *testing.T) {
	cases := []struct {
		pairs   [][]string
		rates   []float64
		queries [][]string
		want    []float64
	}{
		{
			[][]string{{"EUR", "USD"}, {"USD", "MXN"}},
			[]float64{1.25, 20.0},
			[][]string{{"EUR", "MXN"}, {"MXN", "EUR"}, {"USD", "USD"}, {"EUR", "JPY"}, {"JPY", "JPY"}},
			[]float64{25.0, 0.04, 1.0, -1.0, -1.0},
		},
		{
			[][]string{{"GBP", "EUR"}, {"CHF", "SEK"}, {"SEK", "NOK"}},
			[]float64{1.2, 12.0, 0.5},
			[][]string{{"EUR", "GBP"}, {"CHF", "NOK"}, {"NOK", "CHF"}, {"GBP", "NOK"}},
			[]float64{0.8333333333, 6.0, 0.1666666667, -1.0},
		},
		{[][]string{{"EUR", "USD"}}, []float64{1.25}, [][]string{}, []float64{}},
		{
			[][]string{{"USD", "CAD"}, {"CAD", "AUD"}, {"USD", "AUD"}},
			[]float64{1.5, 1.2, 1.8},
			[][]string{{"AUD", "USD"}, {"CAD", "CAD"}},
			[]float64{0.5555555556, 1.0},
		},
	}
	for _, c := range cases {
		if got := ExchangeRates(c.pairs, c.rates, c.queries); !near(got, c.want) {
			t.Errorf("ExchangeRates(%v, %v, %v) = %v, want %v", c.pairs, c.rates, c.queries, got, c.want)
		}
	}
}
