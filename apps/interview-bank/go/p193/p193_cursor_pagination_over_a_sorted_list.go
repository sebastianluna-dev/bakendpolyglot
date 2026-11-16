// 193 · Cursor Pagination Over a Sorted List
package p193

type Page struct {
	Items []int
	Next  *int // nil when there is no next page
}

func FetchPage(ids []int, after *int, limit int) Page {
	start := 0
	if after != nil {
		lo, hi := 0, len(ids) // looking for the first id greater than after
		for lo < hi {
			mid := lo + (hi-lo)/2
			if ids[mid] > *after {
				hi = mid
			} else {
				lo = mid + 1
			}
		}
		start = lo
	}
	end := min(start+limit, len(ids))
	items := ids[start:end]
	if end == len(ids) {
		return Page{Items: items}
	}
	next := items[len(items)-1]
	return Page{Items: items, Next: &next}
}
