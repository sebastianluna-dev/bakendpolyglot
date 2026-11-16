// 199 · Bounded Worker Pool With Ordered Results
package p199

import "sync"

func MapLimit[T, R any](items []T, limit int, fn func(T) R) []R {
	results := make([]R, len(items))
	jobs := make(chan int)
	var wg sync.WaitGroup
	for range min(limit, len(items)) {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for i := range jobs { // each index is received by exactly one worker
				results[i] = fn(items[i])
			}
		}()
	}
	for i := range items {
		jobs <- i
	}
	close(jobs) // ends every worker's range loop
	wg.Wait()
	return results
}
