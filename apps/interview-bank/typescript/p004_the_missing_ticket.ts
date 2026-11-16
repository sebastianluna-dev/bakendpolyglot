// 004 · The Missing Ticket
import assert from "node:assert/strict";

export function missingTicket(scanned: number[]): number {
  const n = scanned.length + 1;
  let total = (n * (n + 1)) / 2;
  for (const t of scanned) total -= t;
  return total;
}

const many = Array.from({ length: 70000 }, (_, i) => i + 1).filter((t) => t !== 31415);

assert.equal(missingTicket([3, 1, 4, 5]), 2);
assert.equal(missingTicket([2]), 1);
assert.equal(missingTicket([1]), 2);
assert.equal(missingTicket([]), 1);
assert.equal(missingTicket(many), 31415);
console.log("004 ok");
