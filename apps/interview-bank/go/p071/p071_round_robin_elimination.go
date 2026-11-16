// 071 · Round-Robin Elimination
package p071

func EliminationOrder(n, k int) []int {
	circle := make([]int, 0, n) // a slice used as a queue: append at the back, reslice at the front
	for i := 1; i <= n; i++ {
		circle = append(circle, i)
	}
	order := make([]int, 0, n)
	for len(circle) > 0 {
		steps := (k - 1) % len(circle) // skip whole laps when k is larger than the circle
		for s := 0; s < steps; s++ {
			circle = append(circle[1:], circle[0])
		}
		order = append(order, circle[0])
		circle = circle[1:]
	}
	return order
}
