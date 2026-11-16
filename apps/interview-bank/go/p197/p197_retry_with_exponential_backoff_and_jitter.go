// 197 · Retry With Exponential Backoff and Jitter
package p197

func Retry[T any](
	operation func() (T, error),
	maxAttempts, baseDelayMs, capMs int,
	sleep func(ms int),
	random func() float64,
) (T, error) {
	ceiling := baseDelayMs
	for attempt := 1; attempt < maxAttempts; attempt++ {
		if v, err := operation(); err == nil {
			return v, nil
		}
		sleep(int(random() * float64(ceiling))) // full jitter: anywhere in [0, ceiling)
		ceiling = min(capMs, ceiling*2)
	}
	return operation() // the last attempt: its error reaches the caller
}
