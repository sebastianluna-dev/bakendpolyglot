// 123 · Is There a Route?
package p123

func HasRoute(n int, edges [][]int, source, target int) bool {
	graph := make([][]int, n)
	for _, e := range edges {
		graph[e[0]] = append(graph[e[0]], e[1])
		graph[e[1]] = append(graph[e[1]], e[0])
	}
	seen := make([]bool, n)
	seen[source] = true
	queue := []int{source}
	for len(queue) > 0 {
		node := queue[0]
		queue = queue[1:]
		if node == target {
			return true
		}
		for _, next := range graph[node] {
			if !seen[next] {
				seen[next] = true // mark when queued, so no node is queued twice
				queue = append(queue, next)
			}
		}
	}
	return false
}
