// 071 · Round-Robin Elimination
import assert from "node:assert/strict";

export function eliminationOrder(n: number, k: number): number[] {
  const circle: number[] = [];
  for (let i = 1; i <= n; i++) circle.push(i);
  let head = 0;
  const order: number[] = [];
  while (head < circle.length) {
    const steps = (k - 1) % (circle.length - head); // skip whole laps when k is larger than the circle
    for (let s = 0; s < steps; s++) circle.push(circle[head++]);
    order.push(circle[head++]);
  }
  return order;
}

assert.deepEqual(eliminationOrder(5, 2), [2, 4, 1, 5, 3]);
assert.deepEqual(eliminationOrder(4, 3), [3, 2, 4, 1]);
assert.deepEqual(eliminationOrder(1, 7), [1]);
assert.deepEqual(eliminationOrder(4, 1), [1, 2, 3, 4]);
assert.deepEqual(eliminationOrder(3, 5), [2, 3, 1]);
assert.deepEqual(eliminationOrder(6, 6), [6, 1, 3, 2, 5, 4]);
console.log("071 ok");
