// 100 · Config Values at a Point in Time
package p100

type entry struct {
	timestamp int
	value     string
}

type ConfigHistory struct {
	history map[string][]entry // oldest first
}

func NewConfigHistory() *ConfigHistory {
	return &ConfigHistory{history: map[string][]entry{}}
}

func (h *ConfigHistory) Set(key, value string, timestamp int) {
	// timestamps only grow, so the slice stays sorted
	h.history[key] = append(h.history[key], entry{timestamp, value})
}

func (h *ConfigHistory) Get(key string, timestamp int) string {
	entries := h.history[key] // nil for an unknown key, which has length 0
	lo, hi := 0, len(entries) // looking for the first entry set after timestamp
	for lo < hi {
		mid := lo + (hi-lo)/2
		if entries[mid].timestamp > timestamp {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	if lo == 0 {
		return ""
	}
	return entries[lo-1].value
}
