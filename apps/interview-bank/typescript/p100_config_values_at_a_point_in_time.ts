// 100 · Config Values at a Point in Time
import assert from "node:assert/strict";

export class ConfigHistory {
  history = new Map<string, Array<[number, string]>>(); // key -> [timestamp, value], oldest first

  set(key: string, value: string, timestamp: number): void {
    let entries = this.history.get(key);
    if (entries === undefined) {
      entries = [];
      this.history.set(key, entries);
    }
    entries.push([timestamp, value]); // timestamps only grow, so the list stays sorted
  }

  get(key: string, timestamp: number): string {
    const entries = this.history.get(key) ?? [];
    let lo = 0;
    let hi = entries.length; // looking for the first entry set after timestamp
    while (lo < hi) {
      const mid = lo + Math.floor((hi - lo) / 2);
      if (entries[mid][0] > timestamp) hi = mid;
      else lo = mid + 1;
    }
    return lo === 0 ? "" : entries[lo - 1][1];
  }
}

const h = new ConfigHistory();
assert.equal(h.get("hold.minutes", 100), "");
h.set("hold.minutes", "10", 100);
h.set("hold.minutes", "15", 250);
h.set("currency", "EUR", 120);
assert.equal(h.get("hold.minutes", 99), "");
assert.equal(h.get("hold.minutes", 100), "10");
assert.equal(h.get("hold.minutes", 249), "10");
assert.equal(h.get("hold.minutes", 250), "15");
assert.equal(h.get("hold.minutes", 1000), "15");
assert.equal(h.get("currency", 119), "");
assert.equal(h.get("currency", 500), "EUR");
assert.equal(h.get("refunds", 500), "");
h.set("hold.minutes", "20", 400);
assert.equal(h.get("hold.minutes", 399), "15");
assert.equal(h.get("hold.minutes", 400), "20");
console.log("100 ok");
