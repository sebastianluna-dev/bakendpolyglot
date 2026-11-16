// 093 · The First Failing Build
import assert from "node:assert/strict";

export function firstFailingBuild(n: number, isBad: (build: number) => boolean): number {
  let lo = 1;
  let hi = n + 1; // n + 1 stands for "no build fails"
  while (lo < hi) {
    const mid = lo + Math.floor((hi - lo) / 2);
    if (isBad(mid)) hi = mid;
    else lo = mid + 1;
  }
  return lo <= n ? lo : -1;
}

let calls = 0;
const failingFrom = (first: number) => (build: number) => {
  calls++;
  return build >= first;
};

assert.equal(firstFailingBuild(5, failingFrom(4)), 4);
assert.equal(firstFailingBuild(3, failingFrom(4)), -1);
assert.equal(firstFailingBuild(1, failingFrom(1)), 1);
assert.equal(firstFailingBuild(1, failingFrom(2)), -1);
assert.equal(firstFailingBuild(5, failingFrom(1)), 1);
calls = 0;
assert.equal(firstFailingBuild(1000, failingFrom(777)), 777);
assert.ok(calls <= 10);
calls = 0;
assert.equal(firstFailingBuild(2_000_000_000, failingFrom(1_999_999_999)), 1_999_999_999);
assert.ok(calls <= 31);
console.log("093 ok");
