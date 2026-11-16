// 196 · Token-Bucket Rate Limiter
package p196

type TokenBucket struct {
	capacity        int64 // in thousandths of a token, so the arithmetic stays exact
	refillPerSecond int64
	tokens          int64
	lastMs          int64
}

func NewTokenBucket(capacity, refillPerSecond int) *TokenBucket {
	c := int64(capacity) * 1000
	return &TokenBucket{capacity: c, refillPerSecond: int64(refillPerSecond), tokens: c}
}

func (b *TokenBucket) Allow(nowMs int64) bool {
	// one token per second is one thousandth per millisecond
	b.tokens = min(b.capacity, b.tokens+(nowMs-b.lastMs)*b.refillPerSecond)
	b.lastMs = nowMs
	if b.tokens < 1000 {
		return false
	}
	b.tokens -= 1000
	return true
}
