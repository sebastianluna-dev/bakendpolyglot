// 151 · Fast Power
import assert from "node:assert/strict";

export function powerMod(x: number, n: number, m: number): number {
  if (n === 0) return 1 % m;
  const half = powerMod(x, Math.floor(n / 2), m); // one recursive call, used twice
  const square = mulMod(half, half, m);
  return n % 2 === 0 ? square : mulMod(square, x % m, m);
}

function mulMod(a: number, b: number, m: number): number {
  return Number((BigInt(a) * BigInt(b)) % BigInt(m)); // a * b can pass 2^53
}

assert.equal(powerMod(3, 13, 100), 23);
assert.equal(powerMod(7, 0, 13), 1);
assert.equal(powerMod(2, 10, 1000), 24);
assert.equal(powerMod(5, 3, 1), 0);
assert.equal(powerMod(0, 0, 7), 1);
assert.equal(powerMod(0, 5, 7), 0);
assert.equal(powerMod(999999999, 2, 1000000007), 64);
assert.equal(powerMod(10, 9, 1000000007), 1000000000);
assert.equal(powerMod(2, 1000000006, 1000000007), 1);
console.log("151 ok");
