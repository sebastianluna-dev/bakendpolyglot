// 062 · Stretches With a Small Product
package p062

func CountSmallProducts(factors []int, limit int) int {
	if limit <= 1 {
		return 0 // every product is at least 1
	}
	left, product, count := 0, 1, 0 // product of factors[left..right]
	for right, f := range factors {
		product *= f
		for product >= limit {
			product /= factors[left]
			left++
		}
		count += right - left + 1 // every stretch that ends at right and starts at left or later
	}
	return count
}
