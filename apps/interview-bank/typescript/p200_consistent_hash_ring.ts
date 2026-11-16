// 200 · Consistent Hash Ring
import assert from "node:assert/strict";

export function fnv1a(s: string): number {
  let h = 0x811c9dc5;
  for (const b of new TextEncoder().encode(s)) {
    h ^= b;
    h = Math.imul(h, 0x01000193); // a 32-bit multiply; plain * loses the low bits past 2^53
  }
  return h >>> 0; // back to unsigned
}

export function mix32(h: number): number {
  h ^= h >>> 16;
  h = Math.imul(h, 0x85ebca6b);
  h ^= h >>> 13;
  h = Math.imul(h, 0xc2b2ae35);
  h ^= h >>> 16;
  return h >>> 0;
}

export function ringHash(s: string): number {
  return mix32(fnv1a(s));
}

export class HashRing {
  replicas: number;
  nodes = new Set<string>();
  points: Array<[number, string]> = []; // [hash, node], sorted by hash, then node

  constructor(replicas: number) {
    this.replicas = replicas;
  }

  addNode(node: string): void {
    if (this.nodes.has(node)) return;
    this.nodes.add(node);
    for (let i = 0; i < this.replicas; i++) this.points.push([ringHash(`${node}#${i}`), node]);
    this.points.sort((a, b) => a[0] - b[0] || (a[1] < b[1] ? -1 : a[1] > b[1] ? 1 : 0));
  }

  removeNode(node: string): void {
    if (!this.nodes.delete(node)) return;
    this.points = this.points.filter((p) => p[1] !== node);
  }

  nodeFor(key: string): string {
    if (this.points.length === 0) return "";
    const h = ringHash(key);
    let lo = 0;
    let hi = this.points.length; // looking for the first point with hash >= h
    while (lo < hi) {
      const mid = lo + Math.floor((hi - lo) / 2);
      if (this.points[mid][0] >= h) hi = mid;
      else lo = mid + 1;
    }
    return this.points[lo % this.points.length][1]; // past the last point: wrap to the first
  }
}

const KEYS = ["book:1", "book:2", "book:3", "book:4", "book:5", "book:6", "book:7", "book:37"];
const owners = (ring: HashRing) => KEYS.map((k) => ring.nodeFor(k).slice(-1)).join(""); // the last letter of each key's node

assert.equal(fnv1a(""), 0x811c9dc5);
assert.equal(fnv1a("a"), 0xe40c292c);
assert.equal(fnv1a("foobar"), 0xbf9cf968);
assert.equal(mix32(0), 0);
assert.equal(ringHash("cache-a#0"), 0x6901e554);
assert.equal(ringHash("book:37"), 0xfcfa1e45);

const ring = new HashRing(3);
assert.equal(ring.nodeFor("book:1"), "");
for (const name of ["cache-a", "cache-b", "cache-c"]) ring.addNode(name);
assert.equal(owners(ring), "babccccc");
ring.addNode("cache-a");
ring.removeNode("cache-z");
assert.equal(owners(ring), "babccccc");
ring.removeNode("cache-b");
assert.equal(owners(ring), "aacccccc"); // only cache-b's keys moved
ring.addNode("cache-b");
assert.equal(owners(ring), "babccccc");
ring.addNode("cache-d");
assert.equal(owners(ring), "babdccdc"); // keys move only to the new node
for (const name of ["cache-a", "cache-b", "cache-c", "cache-d"]) ring.removeNode(name);
assert.equal(ring.nodeFor("book:1"), "");
console.log("200 ok");
