// 136 · Exchange Rates From Known Pairs
package p136

func ExchangeRates(pairs [][]string, rates []float64, queries [][]string) []float64 {
	type edge struct {
		to   string
		rate float64
	}
	graph := map[string][]edge{}
	for i, p := range pairs {
		graph[p[0]] = append(graph[p[0]], edge{p[1], rates[i]})
		graph[p[1]] = append(graph[p[1]], edge{p[0], 1 / rates[i]})
	}
	group := map[string]int{}
	value := map[string]float64{} // units of this currency worth one unit of its group's first
	for start := range graph {
		if _, ok := group[start]; ok {
			continue
		}
		id := len(group)
		group[start] = id
		value[start] = 1
		queue := []string{start}
		for len(queue) > 0 {
			cur := queue[0]
			queue = queue[1:]
			for _, e := range graph[cur] {
				if _, ok := group[e.to]; ok {
					continue
				}
				group[e.to] = id
				value[e.to] = value[cur] * e.rate
				queue = append(queue, e.to)
			}
		}
	}
	answers := make([]float64, len(queries))
	for i, q := range queries {
		gx, okX := group[q[0]]
		gy, okY := group[q[1]]
		if okX && okY && gx == gy {
			answers[i] = value[q[1]] / value[q[0]]
		} else {
			answers[i] = -1.0
		}
	}
	return answers
}
