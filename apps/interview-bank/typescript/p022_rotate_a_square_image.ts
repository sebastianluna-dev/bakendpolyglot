// 022 · Rotate a Square Image
import assert from "node:assert/strict";

export function rotateImage(image: number[][]): void {
  const n = image.length;
  for (let i = 0; i < n; i++) {
    for (let j = i + 1; j < n; j++) {
      [image[i][j], image[j][i]] = [image[j][i], image[i][j]];
    }
  }
  for (const row of image) row.reverse();
}

function rotated(image: number[][]): number[][] {
  rotateImage(image);
  return image;
}

assert.deepEqual(rotated([[1, 2], [3, 4]]), [[3, 1], [4, 2]]);
assert.deepEqual(
  rotated([[11, 12, 13], [21, 22, 23], [31, 32, 33]]),
  [[31, 21, 11], [32, 22, 12], [33, 23, 13]],
);
assert.deepEqual(rotated([]), []);
assert.deepEqual(rotated([[7]]), [[7]]);
assert.deepEqual(
  rotated([[1, 2, 3, 4], [5, 6, 7, 8], [9, 10, 11, 12], [13, 14, 15, 16]]),
  [[13, 9, 5, 1], [14, 10, 6, 2], [15, 11, 7, 3], [16, 12, 8, 4]],
);
console.log("022 ok");
