// 183 · Make Change at the Box Office
import assert from "node:assert/strict";

export function canMakeChange(bills: number[]): boolean {
  let fives = 0;
  let tens = 0;
  for (const bill of bills) {
    if (bill === 5) {
      fives++;
    } else if (bill === 10) {
      if (fives === 0) return false;
      fives--;
      tens++;
    } else if (tens > 0 && fives > 0) {
      // a 10 can only ever change a 20, so spend it before the fives
      tens--;
      fives--;
    } else if (fives >= 3) {
      fives -= 3;
    } else {
      return false;
    }
  }
  return true;
}

assert.equal(canMakeChange([5, 5, 10, 5, 20]), true);
assert.equal(canMakeChange([5, 10, 10, 20]), false);
assert.equal(canMakeChange([]), true);
assert.equal(canMakeChange([10]), false);
assert.equal(canMakeChange([5, 5, 20]), false);
assert.equal(canMakeChange([5, 5, 5, 20]), true);
assert.equal(canMakeChange([5, 5, 5, 5, 10, 20, 10]), true);
console.log("183 ok");
