// 200 · Consistent Hash Ring
package p200

import (
	"cmp"
	"slices"
	"strconv"
	"strings"
)

func Fnv1a(s string) uint32 {
	h := uint32(2166136261)
	for i := 0; i < len(s); i++ { // indexing a string gives its UTF-8 bytes
		h ^= uint32(s[i])
		h *= 16777619 // uint32 arithmetic wraps at 2^32
	}
	return h
}

func Mix32(h uint32) uint32 {
	h ^= h >> 16
	h *= 0x85ebca6b
	h ^= h >> 13
	h *= 0xc2b2ae35
	h ^= h >> 16
	return h
}

func RingHash(s string) uint32 {
	return Mix32(Fnv1a(s))
}

type point struct {
	hash uint32
	node string
}

type HashRing struct {
	replicas int
	nodes    map[string]bool
	points   []point // sorted by hash, then node
}

func NewHashRing(replicas int) *HashRing {
	return &HashRing{replicas: replicas, nodes: map[string]bool{}}
}

func (r *HashRing) AddNode(node string) {
	if r.nodes[node] {
		return
	}
	r.nodes[node] = true
	for i := range r.replicas {
		r.points = append(r.points, point{RingHash(node + "#" + strconv.Itoa(i)), node})
	}
	slices.SortFunc(r.points, func(a, b point) int {
		return cmp.Or(cmp.Compare(a.hash, b.hash), strings.Compare(a.node, b.node))
	})
}

func (r *HashRing) RemoveNode(node string) {
	if !r.nodes[node] {
		return
	}
	delete(r.nodes, node)
	r.points = slices.DeleteFunc(r.points, func(p point) bool { return p.node == node })
}

func (r *HashRing) NodeFor(key string) string {
	if len(r.points) == 0 {
		return ""
	}
	h := RingHash(key)
	lo, hi := 0, len(r.points) // looking for the first point with hash >= h
	for lo < hi {
		mid := lo + (hi-lo)/2
		if r.points[mid].hash >= h {
			hi = mid
		} else {
			lo = mid + 1
		}
	}
	return r.points[lo%len(r.points)].node // past the last point: wrap to the first
}
