// 195 · LRU Cache
package p195

type node struct {
	key, value int
	prev, next *node
}

type LruCache struct {
	capacity int
	nodes    map[int]*node
	head     *node // sentinel: head.next is the most recent, head.prev the least recent
}

func NewLruCache(capacity int) *LruCache {
	head := &node{}
	head.prev, head.next = head, head
	return &LruCache{capacity: capacity, nodes: map[int]*node{}, head: head}
}

func (c *LruCache) Get(key int) int {
	n, ok := c.nodes[key]
	if !ok {
		return -1
	}
	c.unlink(n)
	c.pushFront(n)
	return n.value
}

func (c *LruCache) Put(key, value int) {
	if n, ok := c.nodes[key]; ok {
		n.value = value
		c.unlink(n)
		c.pushFront(n)
		return
	}
	if len(c.nodes) == c.capacity {
		oldest := c.head.prev
		c.unlink(oldest)
		delete(c.nodes, oldest.key)
	}
	n := &node{key: key, value: value}
	c.nodes[key] = n
	c.pushFront(n)
}

func (c *LruCache) unlink(n *node) {
	n.prev.next = n.next
	n.next.prev = n.prev
}

func (c *LruCache) pushFront(n *node) {
	n.prev = c.head
	n.next = c.head.next
	c.head.next.prev = n
	c.head.next = n
}
