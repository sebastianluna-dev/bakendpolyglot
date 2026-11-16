// 076 · Colliding Parcels on a Conveyor
import assert from "node:assert/strict";

export function survivingParcels(parcels: number[]): number[] {
  const track: number[] = [];
  for (const p of parcels) {
    let alive = true;
    while (alive && p < 0 && track.length > 0 && track[track.length - 1] > 0) {
      const top = track[track.length - 1];
      if (top < -p) {
        track.pop();
      } else if (top === -p) {
        track.pop();
        alive = false;
      } else {
        alive = false;
      }
    }
    if (alive) track.push(p);
  }
  return track;
}

assert.deepEqual(survivingParcels([4, 7, -5]), [4, 7]);
assert.deepEqual(survivingParcels([3, 6, -6, -8, 2]), [-8, 2]);
assert.deepEqual(survivingParcels([]), []);
assert.deepEqual(survivingParcels([-3, 4]), [-3, 4]);
assert.deepEqual(survivingParcels([5, -5]), []);
assert.deepEqual(survivingParcels([1, -2, -2, -2]), [-2, -2, -2]);
assert.deepEqual(survivingParcels([10, 2, -5]), [10]);
console.log("076 ok");
