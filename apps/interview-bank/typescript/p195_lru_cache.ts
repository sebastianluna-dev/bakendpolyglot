// 195 · LRU Cache
import assert from "node:assert/strict";

class CacheNode {
  key: number;
  value: number;
  prev: CacheNode = this;
  next: CacheNode = this;

  constructor(key: number, value: number) {
    this.key = key;
    this.value = value;
  }
}

export class LruCache {
  capacity: number;
  nodes = new Map<number, CacheNode>();
  head = new CacheNode(0, 0); // sentinel: head.next is the most recent, head.prev the least recent

  constructor(capacity: number) {
    this.capacity = capacity;
  }

  get(key: number): number {
    const node = this.nodes.get(key);
    if (node === undefined) return -1;
    this.unlink(node);
    this.pushFront(node);
    return node.value;
  }

  put(key: number, value: number): void {
    const existing = this.nodes.get(key);
    if (existing !== undefined) {
      existing.value = value;
      this.unlink(existing);
      this.pushFront(existing);
      return;
    }
    if (this.nodes.size === this.capacity) {
      const oldest = this.head.prev;
      this.unlink(oldest);
      this.nodes.delete(oldest.key);
    }
    const node = new CacheNode(key, value);
    this.nodes.set(key, node);
    this.pushFront(node);
  }

  private unlink(node: CacheNode): void {
    node.prev.next = node.next;
    node.next.prev = node.prev;
  }

  private pushFront(node: CacheNode): void {
    node.prev = this.head;
    node.next = this.head.next;
    this.head.next.prev = node;
    this.head.next = node;
  }
}

const c = new LruCache(2);
c.put(1, 10);
c.put(2, 20);
assert.equal(c.get(1), 10);
c.put(3, 30); // evicts 2
assert.equal(c.get(2), -1);
assert.equal(c.get(3), 30);
c.put(1, 11); // an update counts as a use
c.put(4, 40); // evicts 3
assert.equal(c.get(3), -1);
assert.equal(c.get(1), 11);
assert.equal(c.get(4), 40);

const one = new LruCache(1);
one.put(5, 50);
assert.equal(one.get(5), 50);
one.put(6, 60);
assert.equal(one.get(5), -1);
assert.equal(one.get(6), 60);
assert.equal(one.get(7), -1);
one.put(6, 61);
assert.equal(one.get(6), 61);
console.log("195 ok");
