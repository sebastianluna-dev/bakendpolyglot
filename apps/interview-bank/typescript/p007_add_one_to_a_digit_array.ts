// 007 · Add One to a Digit Array
import assert from "node:assert/strict";

export function addOne(digits: number[]): number[] {
  const result = [...digits];
  for (let i = result.length - 1; i >= 0; i--) {
    if (result[i] < 9) {
      result[i]++;
      return result;
    }
    result[i] = 0;
  }
  return [1, ...result]; // every digit was 9
}

const input = [1, 2, 9];
assert.deepEqual(addOne(input), [1, 3, 0]);
assert.deepEqual(input, [1, 2, 9]);
assert.deepEqual(addOne([9, 9, 9]), [1, 0, 0, 0]);
assert.deepEqual(addOne([4, 5]), [4, 6]);
assert.deepEqual(addOne([0]), [1]);
assert.deepEqual(addOne(new Array<number>(20).fill(9)), [1, ...new Array<number>(20).fill(0)]);
console.log("007 ok");
