// 001 · Duplicate Barcodes
import assert from "node:assert/strict";

export function hasDuplicate(barcodes: string[]): boolean {
  const seen = new Set<string>();
  for (const code of barcodes) {
    if (seen.has(code)) return true;
    seen.add(code);
  }
  return false;
}

assert.equal(hasDuplicate(["SW-1042", "SW-0007", "SW-2210", "SW-0007"]), true);
assert.equal(hasDuplicate(["SW-1042", "SW-0007", "SW-2210"]), false);
assert.equal(hasDuplicate(["SW-0007", "sw-0007"]), false);
assert.equal(hasDuplicate(["SW-1042"]), false);
assert.equal(hasDuplicate([]), false);
console.log("001 ok");
